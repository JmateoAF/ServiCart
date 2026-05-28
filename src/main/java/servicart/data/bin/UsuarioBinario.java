package servicart.data.bin;

import servicart.core.models.Usuario;
import servicart.data.interfaces.InterfazUsuario;

import java.util.List;

public class UsuarioBinario implements InterfazUsuario {
    @Override
    public boolean insertar(Usuario usuario) {
        return false;
    }

    @Override
    public boolean actualizar(Usuario entidad) {
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        return false;
    }

    @Override
    public List<Usuario> listarTodos() {
        return List.of();
    }
    //AQUÍ SE PUEDE APLICAR POLIMORFISMO DEPENDE A COMO SE LLAME A LAS CLASES, YA SEA HA SQL O BINARIO
}
