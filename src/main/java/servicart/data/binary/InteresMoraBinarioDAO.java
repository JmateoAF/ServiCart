package servicart.data.binary;

import servicart.models.entities.InteresMora;

public class InteresMoraBinarioDAO  extends GenericBinarioDAO<InteresMora> {
    public InteresMoraBinarioDAO() { super("bin/InteresMora.bin"); } //Archivo único para esta entidad

    @Override
    protected String getId(InteresMora interesMora) { return String.valueOf(interesMora.getId()); } //Identificador natural
}