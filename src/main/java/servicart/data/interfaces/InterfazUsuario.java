package servicart.data.interfaces;

import servicart.core.models.Usuario;
import java.util.List;

//TAMBIÉN SE PUEDEN HERADAR METODOS ENTRE LAS INTERFACES
public interface InterfazUsuario extends InterfazCRUD<Usuario>{
    boolean insertar(Usuario usuario);
    List<Usuario> listarTodos();
}
