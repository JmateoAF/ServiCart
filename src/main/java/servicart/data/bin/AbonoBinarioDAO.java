package servicart.data.bin;

import servicart.domain.interfaces.CrudDAO;
import servicart.domain.models.entidades.Abono;

import java.util.List;
import java.util.Optional;

public class AbonoBinarioDAO implements CrudDAO<Abono> {
    @Override
    public void save(Abono entidad) {

    }

    @Override
    public Optional<Abono> findId(String id) {
        return Optional.empty();
    }

    @Override
    public List<Abono> findAll() {
        return List.of();
    }

    @Override
    public void update(Abono entidad) {

    }

    @Override
    public void delete(String id) {

    }
}
