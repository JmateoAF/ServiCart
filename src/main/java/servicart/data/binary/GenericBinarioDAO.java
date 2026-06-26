package servicart.data.binary;

import servicart.data.interfaces.CrudDAO; // ubicación real según el árbol
import servicart.domain.models.entidades.Identificable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación base de CRUD sobre archivo binario.
 * Cada subclase define cómo obtener el ID y decide si una entidad está activa.
 * La serialización se maneja aquí (no en ConexionBinario).
 * @param <T> tipo de entidad serializable
 */
public abstract class GenericBinarioDAO<T extends Serializable> implements CrudDAO<T> {

    /* Es {@code protected} para que las subclases puedan, si lo necesitan, realizar
     * operaciones excepcionales directamente sobre el archivo (por ejemplo, backups,
     * compactación o lecturas auxiliares) sin duplicar la configuración de la ruta.
     */
    protected final ConexionBinario conexion;

    /* Caché en memoria de la lista completa.
     * Se declara {@code protected} para que las subclases puedan invalidarla
     * manualmente cuando implementen métodos adicionales que modifiquen el archivo.
     */
    protected List<T> cache = null;

    public GenericBinarioDAO(String nombreArchivo) {
        this.conexion = new ConexionBinario(nombreArchivo);
    }

    /** Identificador único de la entidad. */
    protected abstract String getId(T entidad);

    /* Es {@code protected} para que solo los DAO de la misma jerarquía puedan
     * cambiar el criterio de activación, manteniéndolo oculto para el resto del sistema. */
    protected boolean isActivo(T entidad) { return true; }

    /**
     * Lee la lista completa desde el archivo.
     * Ahora utiliza InputStream crudo y lo envuelve explícitamente.
     *
     * Es {@code protected} para que las subclases puedan acceder a la lista cruda
     * en operaciones complejas (cálculos, reportes, migraciones) sin depender
     * únicamente de los métodos públicos CRUD.
     *
     * La anotación @SuppressWarnings("unchecked") la agregué para silenciar la advertencia del compilador en la línea
     */
    @SuppressWarnings("unchecked")
    protected List<T> leerTodos() {
        try {
            if (conexion.estaVacio()) {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (ObjectInputStream ois = new ObjectInputStream(conexion.newInputStream())) {
            return (List<T>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al leer archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Persiste la lista completa de manera atómica.
     * Es {@code protected} para que las subclases puedan acceder a la lista cruda
     * en operaciones complejas (cálculos, reportes, migraciones) sin depender
     * únicamente de los métodos públicos CRUD.
     * Convierte la lista en bytes mediante ObjectOutputStream.
     */
    protected void guardarTodos(List<T> lista) {
        try {
            conexion.escribirAtomicamente(os -> {
                try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
                    oos.writeObject(lista);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivo: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<T> findId(String id) {
        List<T> lista = (cache != null) ? cache : leerTodos();
        return lista.stream()
                .filter(e -> isActivo(e) && getId(e).equals(id))
                .findFirst();
    }

    @Override
    public List<T> findAll() {
        if (cache == null) cache = leerTodos();
        return cache.stream()
                .filter(this::isActivo)
                .collect(Collectors.toList());
    }

    @Override
    public void save(T entidad) {
        List<T> lista = (cache != null) ? cache : leerTodos();

        // ── AUTOINCREMENTO para entidades con ID numérico ──
        if (entidad instanceof Identificable) { //verifica si el objeto entidad implementa la interfaz Identificable
            int maxId = lista.stream()
                    .filter(e -> e instanceof Identificable)
                    .mapToInt(e -> ((Identificable) e).getId())
                    .max().orElse(0);
            ((Identificable) entidad).setId(maxId + 1);
        }

        String id = getId(entidad);
        if (lista.stream().anyMatch(e -> getId(e).equals(id))) {
            throw new RuntimeException("Ya existe un registro con ID " + id);
        }

        lista.add(entidad);
        guardarTodos(lista);
        cache = lista;
    }
/*
* lista.set(index, entidad) es un metodo de la interfaz
* java.util.List que reemplaza el elemento que se encuentra
* en la posición index por el nuevo objeto entidad.
* */
    @Override
    public void update(T entidad) {
        List<T> lista = (cache != null) ? cache : leerTodos();
        String id = getId(entidad);
        int index = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (getId(lista.get(i)).equals(id)) { index = i; break; }
        }
        if (index == -1) throw new RuntimeException("Registro con ID " + id + " no encontrado");
        lista.set(index, entidad);
        guardarTodos(lista);
        cache = lista;
    }
    //Eliminación física
    @Override
    public void delete(String id) {
        List<T> lista = (cache != null) ? cache : leerTodos();
        boolean removido = lista.removeIf(e -> getId(e).equals(id));
        if (!removido) throw new RuntimeException("Registro con ID " + id + " no encontrado");
        guardarTodos(lista);
        cache = lista;
    }
}