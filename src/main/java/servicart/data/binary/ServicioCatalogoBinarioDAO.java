package servicart.data.binary;

import servicart.entities.ServicioCatalogo;

public class ServicioCatalogoBinarioDAO extends GenericBinarioDAO<ServicioCatalogo> {
    public ServicioCatalogoBinarioDAO() { super("bin/servicioCatalogo.bin"); }

    @Override
    protected String getId(ServicioCatalogo entidad) { return String.valueOf(entidad.getId()); }
}