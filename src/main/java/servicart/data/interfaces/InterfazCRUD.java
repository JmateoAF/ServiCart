package servicart.data.interfaces;

import java.util.List;

public interface InterfazCRUD<T> {
    //SI A LA FINAL "TODO LO QUE SABEN HACER" LO COMPARTEN TODAS LAS CLASES INTERMEDIAS
    //SE PUEDE CREAR UNA INTERFAZ GENERAL Y QUE SE UTILICEN EN TODAS LAS CLASES INTERMEDIAS
    //LAS QUE SE USAN PARA CONECTAR A LA BASE DE DATOS
    boolean insertar(T entidad);
    boolean actualizar(T entidad);
    boolean eliminar(int id);
    List<T> listarTodos();
}
