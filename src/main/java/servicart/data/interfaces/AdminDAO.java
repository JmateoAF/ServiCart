package servicart.data.interfaces;

import java.util.Optional;

public interface AdminDAO<T>{
    Optional<T> credenciales(String nombre, String contrasenia);
}
