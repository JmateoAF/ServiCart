package servicart.data.binary;

import servicart.entities.Abono;

public class AbonoBinarioDAO extends GenericBinarioDAO<Abono> {
    public AbonoBinarioDAO() { super("bin/abono.bin"); } // archivo único para esta entidad

    @Override
    protected String getId(Abono abono) { return String.valueOf(abono.getId()); }  //Identificador natural
}