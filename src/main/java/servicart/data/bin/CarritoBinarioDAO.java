package servicart.data.bin;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entidades.Carrito;

import java.util.List;
import java.util.Optional;

public class CarritoBinarioDAO implements CrudDAO<Carrito> {
    @Override
    public void save(Carrito entidad) {

    }

    @Override
    public Optional<Carrito> findId(String id) {
        return Optional.empty();
    }

    @Override
    public List<Carrito> findAll() {
        return List.of();
    }

    @Override
    public void update(Carrito entidad) {

    }

    @Override
    public void delete(String id) {

    }
}
