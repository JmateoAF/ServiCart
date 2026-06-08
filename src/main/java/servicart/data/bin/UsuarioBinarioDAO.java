package servicart.data.bin;

import servicart.domain.models.Usuario;
import servicart.domain.interfaces.CrudDAO;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioBinarioDAO implements CrudDAO<Usuario> {

    // Mismo nombre de archivo que ConexionBinario, sin necesidad de modificar esa clase
    private static final String NOMBRE_ARCHIVO = "usuarios.bin";

    private List<Usuario> cache = null;

    /**
     * Lee todos los usuarios desde el archivo binario gestionado por ConexionBinario.
     */
    private List<Usuario> leerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            if (ConexionBinario.estaVacio()) {
                return usuarios;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al verificar si el archivo está vacío: " + e.getMessage(), e);
        }
        try (DataInputStream dis = ConexionBinario.abrirParaLectura()) {
            while (true) {
                try {
                    String cedula = dis.readUTF();
                    String nombre = dis.readUTF();
                    String email = dis.readUTF();
                    String telefono = dis.readUTF();
                    boolean activo = dis.readBoolean();
                    usuarios.add(new Usuario(cedula, nombre, email, telefono, activo));
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    /**
     * Guarda la lista de usuarios de forma atómica:
     * escribe en un archivo temporal y luego lo mueve al destino original.
     */
    private void guardarTodos(List<Usuario> usuarios) {
        Path archivoOriginal = Paths.get(NOMBRE_ARCHIVO);
        Path archivoTemporal = null;
        try {
            archivoTemporal = Files.createTempFile("usuarios", ".tmp");
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivoTemporal.toFile()))) {
                for (Usuario u : usuarios) {
                    dos.writeUTF(u.getCedula());
                    dos.writeUTF(u.getNombre());
                    dos.writeUTF(u.getEmail());
                    dos.writeUTF(u.getTelefono());
                    dos.writeBoolean(u.isActivo());
                }
            }
            // Reemplazo atómico (si el sistema de archivos lo soporta)
            Files.move(archivoTemporal, archivoOriginal,
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            // Si algo falla, intentamos eliminar el archivo temporal
            if (archivoTemporal != null) {
                try {
                    Files.deleteIfExists(archivoTemporal);
                } catch (IOException ignored) {}
            }
            throw new RuntimeException("Error al guardar usuarios: " + e.getMessage(), e);
        }
    }


    public Optional<Usuario> findId(String id) {
        List<Usuario> lista = (cache != null) ? cache : leerTodos();
        return lista.stream()
                .filter(u -> u.getCedula().equals(id))
                .findFirst();
    }


    public List<Usuario> findAll() {
        if (cache == null) {
            cache = leerTodos();
        }
        // Devuelve una copia para proteger la lista interna
        return new ArrayList<>(cache);
    }

    @Override
    public void save(Usuario entidad) {
        List<Usuario> usuarios = (cache != null) ? cache : leerTodos();
        boolean existe = usuarios.stream().anyMatch(u -> u.getCedula().equals(entidad.getCedula()));
        if (existe) {
            throw new RuntimeException("Ya existe un usuario con cédula " + entidad.getCedula());
        }
        usuarios.add(entidad);
        guardarTodos(usuarios);
        cache = usuarios;
    }

    @Override
    public void update(Usuario entidad) {
        List<Usuario> usuarios = (cache != null) ? cache : leerTodos();
        int index = -1;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCedula().equals(entidad.getCedula())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new RuntimeException("Usuario con cédula " + entidad.getCedula() + " no encontrado");
        }
        usuarios.set(index, entidad);
        guardarTodos(usuarios);
        cache = usuarios;
    }

    @Override
    public void delete(String id) {
        List<Usuario> usuarios = (cache != null) ? cache : leerTodos();
        for (Usuario u : usuarios) {
            if (u.getCedula().equals(id)) {
                if (!u.isActivo()) {
                    // Ya estaba inactivo, no hacemos nada (evita escritura innecesaria)
                    return;
                }
                u.setActivo(false);
                guardarTodos(usuarios);
                cache = usuarios;
                return;
            }
        }
        throw new RuntimeException("Usuario con cédula " + id + " no encontrado");
    }
}