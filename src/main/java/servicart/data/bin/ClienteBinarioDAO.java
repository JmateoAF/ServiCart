package servicart.data.bin;

import servicart.domain.models.Cliente;
import servicart.domain.interfaces.CrudDAO;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteBinarioDAO implements CrudDAO<Cliente> {

    // Mismo nombre de archivo que ConexionBinario, sin necesidad de modificar esa clase
    private static final String NOMBRE_ARCHIVO = "usuarios.bin";

    private List<Cliente> cache = null;

    /**
     * Lee todos los usuarios desde el archivo binario gestionado por ConexionBinario.
     */
    private List<Cliente> leerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            if (ConexionBinario.estaVacio()) {
                return clientes;
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
                    clientes.add(new Cliente(cedula, nombre, email, telefono, activo));
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer usuarios: " + e.getMessage(), e);
        }
        return clientes;
    }

    /**
     * Guarda la lista de usuarios de forma atómica:
     * escribe en un archivo temporal y luego lo mueve al destino original.
     */
    private void guardarTodos(List<Cliente> clientes) {
        Path archivoOriginal = Paths.get(NOMBRE_ARCHIVO);
        Path archivoTemporal = null;
        try {
            archivoTemporal = Files.createTempFile("usuarios", ".tmp");
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivoTemporal.toFile()))) {
                for (Cliente u : clientes) {
                    dos.writeUTF(u.getCedula());
                    dos.writeUTF(u.getNombre());
                    dos.writeUTF(u.getEmail());
                    dos.writeUTF(u.getCelular());
                    dos.writeBoolean(u.getActivo());
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


    public Optional<Cliente> findId(String id) {
        List<Cliente> lista = (cache != null) ? cache : leerTodos();
        return lista.stream()
                .filter(u -> u.getCedula().equals(id))
                .findFirst();
    }


    public List<Cliente> findAll() {
        if (cache == null) {
            cache = leerTodos();
        }
        // Devuelve una copia para proteger la lista interna
        return new ArrayList<>(cache);
    }

    @Override
    public void save(Cliente entidad) {
        List<Cliente> clientes = (cache != null) ? cache : leerTodos();
        boolean existe = clientes.stream().anyMatch(u -> u.getCedula().equals(entidad.getCedula()));
        if (existe) {
            throw new RuntimeException("Ya existe un usuario con cédula " + entidad.getCedula());
        }
        clientes.add(entidad);
        guardarTodos(clientes);
        cache = clientes;
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
            throw new RuntimeException("Usuario con cédula " + entidad.getCedula() + " no encontrado");
        }
        clientes.set(index, entidad);
        guardarTodos(clientes);
        cache = clientes;
    }

    @Override
    public void delete(String cedula) {
        List<Cliente> clientes = (cache != null) ? cache : leerTodos();
        for (Cliente u : clientes) {
            if (u.getCedula().equals(cedula)) {
                if (!u.getActivo()) {
                    // Ya estaba inactivo, no hacemos nada (evita escritura innecesaria)
                    return;
                }
                u.setActivo(false);
                guardarTodos(clientes);
                cache = clientes;
                return;
            }
        }
        throw new RuntimeException("Usuario con cédula " + cedula + " no encontrado");
    }
}