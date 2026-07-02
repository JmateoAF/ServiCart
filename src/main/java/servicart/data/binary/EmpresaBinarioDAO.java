package servicart.data.binary;

import servicart.entities.Empresa;

public class EmpresaBinarioDAO extends GenericBinarioDAO<Empresa> {
    public EmpresaBinarioDAO() { super("bin/empresa.bin"); } // archivo único para esta entidad

    @Override
    protected String getId(Empresa empresa) { return String.valueOf(empresa.getId()); }  //Identificador natural
}
