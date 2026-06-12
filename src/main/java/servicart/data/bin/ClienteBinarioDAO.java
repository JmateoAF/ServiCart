package servicart.data.bin;

import servicart.domain.models.entidades.Cliente;
import servicart.data.interfaces.CrudDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClienteBinarioDAO implements CrudDAO<Cliente> {

    private final ConexionBinario conexion = new ConexionBinario("clientes.ser");
    private List<Cliente> cache = null;               // evita leer el archivo en cada operación

    // Carga la lista completa; si el archivo está vacío devuelve lista nueva.
    @SuppressWarnings("unchecked")
    private List<Cliente> leerTodos() {
        try {
            if (conexion.estaVacio()) {
                return new ArrayList<>();             // archivo nuevo, sin datos
            }
            return conexion.leerObjeto();             // lectura delegada
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al leer clientes: " + e.getMessage(), e);
        }
    }

    // Persiste la lista completa usando un Consumer (escritura delegada al DAO).
    private void guardarTodos(List<Cliente> clientes) {
        try {
            conexion.guardarAtomicamente(oos -> {    // Consumer recibe el stream
                try {
                    oos.writeObject(clientes);       // serializa la lista entera
                } catch (IOException e) {
                    throw new RuntimeException("Error al escribir clientes", e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar clientes: " + e.getMessage(), e);
        }
    }

    // Filtra solo clientes con activo == 1 usando Streams.
    private List<Cliente> filtrarActivos(List<Cliente> todos) {
        return todos.stream()
                .filter(c -> c.getActivo() == 1)
                .collect(Collectors.toList());       // Collectors.toList() crea lista mutable
    }

    @Override
    public Optional<Cliente> findId(String id) {
        List<Cliente> lista = (cache != null) ? cache : leerTodos();
        return lista.stream()
                .filter(u -> u.getCedula().equals(id) && u.getActivo() == 1)
                .findFirst();                        // devuelve Optional vacío si no encuentra
    }

    @Override
    public List<Cliente> findAll() {
        if (cache == null) {
            cache = leerTodos();                     // carga inicial o refresco
        }
        return filtrarActivos(cache);                // devuelve copia solo de activos
    }

    @Override
    public void save(Cliente entidad) {
        List<Cliente> clientes = (cache != null) ? cache : leerTodos();
        if (clientes.stream().anyMatch(u -> u.getCedula().equals(entidad.getCedula()))) {
            throw new RuntimeException("Ya existe un cliente con cédula " + entidad.getCedula());
        }
        clientes.add(entidad);
        guardarTodos(clientes);
        cache = clientes;                            // mantiene la caché coherente
    }

    @Override
    public void update(Cliente entidad) {
        List<Cliente> clientes = (cache != null) ? cache : leerTodos();
        int index = -1;
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getCedula().equals(entidad.getCedula())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new RuntimeException("Cliente con cédula " + entidad.getCedula() + " no encontrado");
        }
        clientes.set(index, entidad);                // reemplazo en la lista
        guardarTodos(clientes);
        cache = clientes;
    }

    @Override
    public void delete(String cedula) {
        List<Cliente> clientes = (cache != null) ? cache : leerTodos();
        for (Cliente u : clientes) {
            if (u.getCedula().equals(cedula)) {
                if (u.getActivo() == 0) {
                    cache = clientes;                // ya estaba inactivo, no guarda
                    return;
                }
                u.setActivo(0);                      // borrado lógico
                guardarTodos(clientes);
                cache = clientes;
                return;
            }
        }
        throw new RuntimeException("Cliente con cédula " + cedula + " no encontrado");
    }
}