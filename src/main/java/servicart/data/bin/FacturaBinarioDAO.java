package servicart.data.bin;

import servicart.domain.interfaces.CrudDAO;
import servicart.domain.models.entidades.Factura;

import java.util.List;
import java.util.Optional;

public class FacturaBinarioDAO implements CrudDAO<Factura> {
    @Override
    public void save(Factura entidad) {

    }

    @Override
    public Optional<Factura> findId(String id) {
        return Optional.empty();
    }

    @Override
    public List<Factura> findAll() {
        return List.of();
    }

    @Override
    public void update(Factura entidad) {

    }

    @Override
    public void delete(String id) {

    }
}
