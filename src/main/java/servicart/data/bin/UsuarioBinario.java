package servicart.data.bin;

import servicart.core.models.Usuario;
import servicart.data.interfaces.InterfazCRUD;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioBinario implements InterfazCRUD<Usuario> {

    // Lee todos los usuarios del archivo binario
    private List<Usuario> leerTodosDesdeArchivo() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();

        // Si el archivo está vacío, no hay nada que leer
        if (ConexionArchBin.estaVacio()) {
            return usuarios;
        }

        try (DataInputStream dis = ConexionArchBin.abrirParaLectura()) {
            while (true) {
                try {
                    int id = dis.readInt();
                    String nombre = dis.readUTF();
                    String cedula = dis.readUTF();
                    usuarios.add(new Usuario(id, cedula, nombre));
                } catch (EOFException e) {
                    // Se terminó el archivo, salimos del bucle
                    break;
                }
            }
        }
        return usuarios;
    }

    // sobrescribe
    private void guardarTodosEnArchivo(List<Usuario> usuarios) throws IOException {
        try (DataOutputStream dos = ConexionArchBin.abrirParaEscritura()) {
            for (Usuario u : usuarios) {
                dos.writeInt(u.getId());
                dos.writeUTF(u.getNombre());
                dos.writeUTF(u.getCedula());
            }
        }
    }

    @Override
    public boolean actualizar(Usuario usuarioAActualizar) {
        try {
            List<Usuario> usuarios = leerTodosDesdeArchivo();
            boolean encontrado = false;

            for (Usuario u : usuarios) {
                if (u.getId() == usuarioAActualizar.getId()) {
                    u.setNombre(usuarioAActualizar.getNombre());
                    u.setCedula(usuarioAActualizar.getCedula());
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                guardarTodosEnArchivo(usuarios);
                return true;
            } else {
                System.out.println("Usuario con ID " + usuarioAActualizar.getId() + " no encontrado");
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
}