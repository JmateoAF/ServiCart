package servicart.data.binary;

import servicart.entities.Contrato;
import servicart.entities.enums.CausaTerminacion;

public class ContratoBinarioDAO extends GenericBinarioDAO<Contrato> {
    public ContratoBinarioDAO() {
        super("bin/contrato.bin"); //Archivo único para esta entidad
    }

    @Override
    protected String getId(Contrato contrato) {
        return String.valueOf(contrato.getId()); //Identificador natural
    }

    @Override
    protected boolean isActivo(Contrato contrato) {
        return contrato.getCausaTerminacion() == CausaTerminacion.ACTIVO; //Borrado lógico: activo = 1
    }
}
