package servicart.data.bin;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entidades.InteresMora;

import java.util.List;
import java.util.Optional;

public class InteresMoraBinarioDAO implements CrudDAO<InteresMora> {
    @Override
    public void save(InteresMora entidad) {

    }

    @Override
    public Optional<InteresMora> findId(String id) {
        return Optional.empty();
    }

    @Override
    public List<InteresMora> findAll() {
        return List.of();
    }

    @Override
    public void update(InteresMora entidad) {

    }

    @Override
    public void delete(String id) {

    }
}
