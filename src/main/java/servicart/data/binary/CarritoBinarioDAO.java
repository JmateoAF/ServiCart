package servicart.data.binary;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entidades.Carrito;

public class CarritoBinarioDAO extends GenericBinarioDAO<Carrito> implements CrudDAO<Carrito> {
    public CarritoBinarioDAO() {
        super("bin/carrito.bin");               // archivo único para esta entidad
    }

    @Override
    protected String getId(Carrito entidad) {
        return String.valueOf(entidad.getId());          // identificador natural
    }
}
