package servicart.data.bin;

import servicart.domain.models.Cliente;
import servicart.domain.interfaces.CrudDAO;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClienteBinarioDAO implements CrudDAO<Cliente> {

    private final ConexionBinario conexion = new ConexionBinario("clientes.bin");
    private List<Cliente> cache = null;

    // Lee todos los clientes del archivo binario, incluidos los inactivos
    private List<Cliente> leerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            if (conexion.estaVacio()) {
                return clientes;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al verificar si el archivo está vacío: " + e.getMessage(), e);
        }
        try (DataInputStream dis = conexion.abrirParaLectura()) {
            while (true) {
                try {
                    String cedula = dis.readUTF();
                    String nombre = dis.readUTF();
                    String email = dis.readUTF();
                    String celular = dis.readUTF();
                    int activo = dis.readInt();
                    clientes.add(new Cliente(cedula, nombre, email, celular, activo));
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    // Guarda la lista completa de forma atómica
    private void guardarTodos(List<Cliente> clientes) {
        try {
            conexion.guardarAtomicamente(dos -> {
                for (Cliente c : clientes) {
                    try {
                        dos.writeUTF(c.getCedula());
                        dos.writeUTF(c.getNombre());
                        dos.writeUTF(c.getEmail());
                        dos.writeUTF(c.getCelular());
                        dos.writeInt(c.getActivo());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar clientes: " + e.getMessage(), e);
        }
    }

    // Filtra solo los clientes activos
    private List<Cliente> filtrarActivos(List<Cliente> todos) {
        return todos.stream()
                .filter(c -> c.getActivo() == 1)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Cliente> findId(String id) {
        List<Cliente> lista = (cache != null) ? cache : leerTodos();
        return lista.stream()
                .filter(u -> u.getCedula().equals(id) && u.getActivo() == 1)
                .findFirst();
    }

    @Override
    public List<Cliente> findAll() {
        if (cache == null) {
            cache = leerTodos();
        }
        // Devuelve copia de solo los activos
        return filtrarActivos(cache);
    }

    @Override
    public void save(Cliente entidad) {
        List<Cliente> clientes = (cache != null) ? cache : leerTodos();
        boolean existe = clientes.stream().anyMatch(u -> u.getCedula().equals(entidad.getCedula()));
        if (existe) {
            throw new RuntimeException("Ya existe un cliente con cédula " + entidad.getCedula());
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
            throw new RuntimeException("Cliente con cédula " + entidad.getCedula() + " no encontrado");
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
                if (u.getActivo() == 0) {
                    cache = clientes;
                    return;
                }
                u.setActivo(0);
                guardarTodos(clientes);
                cache = clientes;
                return;
            }
        }
        throw new RuntimeException("Cliente con cédula " + cedula + " no encontrado");
    }
}