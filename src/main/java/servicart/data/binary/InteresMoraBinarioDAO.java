package servicart.data.binary;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entidades.InteresMora;

public class InteresMoraBinarioDAO  extends GenericBinarioDAO<InteresMora> implements CrudDAO<InteresMora> {
    public InteresMoraBinarioDAO() {
        super("bin/InteresMora.bin");               // archivo único para esta entidad
    }

    @Override
    protected String getId(InteresMora entidad) {
        return String.valueOf(entidad.getId());          // identificador natural
    }
}
