package servicart.data.bin;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entidades.CorteServicio;

import java.util.List;
import java.util.Optional;

public class CorteServicioBinarioDAO implements CrudDAO<CorteServicio> {
    @Override
    public void save(CorteServicio entidad) {

    }

    @Override
    public Optional<CorteServicio> findId(String id) {
        return Optional.empty();
    }

    @Override
    public List<CorteServicio> findAll() {
        return List.of();
    }

    @Override
    public void update(CorteServicio entidad) {

    }

    @Override
    public void delete(String id) {

    }
}
