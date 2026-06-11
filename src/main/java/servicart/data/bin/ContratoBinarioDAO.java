package servicart.data.bin;

import servicart.domain.interfaces.CrudDAO;
import servicart.domain.models.entidades.Contrato;

import java.util.List;
import java.util.Optional;

public class ContratoBinarioDAO implements CrudDAO<Contrato> {
    @Override
    public void save(Contrato entidad) {

    }

    @Override
    public Optional<Contrato> findId(String id) {
        return Optional.empty();
    }

    @Override
    public List<Contrato> findAll() {
        return List.of();
    }

    @Override
    public void update(Contrato entidad) {

    }

    @Override
    public void delete(String id) {

    }
}
