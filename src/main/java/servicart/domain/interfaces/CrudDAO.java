package servicart.domain.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T> {
    void save(T entidad);
    Optional<T> findId(String cedula);
    List<T> findAll();
    void update(T entidad);
    void delete(String cedula);
}
