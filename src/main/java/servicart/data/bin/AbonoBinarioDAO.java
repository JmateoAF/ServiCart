package servicart.data.bin;

import servicart.data.interfaces.CrudDAO;
import servicart.models.entidades.Abono;

public class AbonoBinarioDAO extends GenericBinarioDAO<Abono> implements CrudDAO<Abono> {
    public AbonoBinarioDAO() {
        super("bin/abono.bin");               // archivo único para esta entidad
    }

    @Override
    protected String getId(Abono entidad) {
        return String.valueOf(entidad.getId());          // identificador natural
    }
}
