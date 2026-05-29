package servicart.data.bin;

import servicart.core.models.Usuario;
import servicart.data.interfaces.InterfazUsuario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static servicart.data.bin.ConexionArchBin.estaVacio;

public class UsuarioBinario implements InterfazUsuario {

private List<Usuario> leerTodosDesdeArchivo () throws IOException{
    List<Usuario> usuarios = new ArrayList<>();
    // Comprobar si el archivo existe y no está vacío
        if (ConexionArchBin.estaVacio()) {
            return usuarios; // vacío
        }

    return usuarios;
}

private void guardarTodosEnArchivo(List<Usuario> usuarios) throws IOException {

}
    @Override
    public boolean insertar(Usuario usuario) {
        try(DataOutputStream bin = ConexionArchBin.abrirParaEscritura()) {
            List<Usuario> Usuarios= new ArrayList<>();
            Usuarios=leerTodosDesdeArchivo();
            Usuarios.add(usuario);
            guardarTodosEnArchivo(Usuarios);
            return true;
        } catch (IOException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        try(DataOutputStream bin = ConexionArchBin.abrirParaEscritura()) {
            System.out.println("Funciona");
            return true;
        } catch (IOException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        try(DataOutputStream bin = ConexionArchBin.abrirParaEscritura()) {
            if (estaVacio()) {
                System.out.println("El archivo está vacío");
                return false;
            } else {

            return true;
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        try {
            return leerTodosDesdeArchivo();
        } catch (IOException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

