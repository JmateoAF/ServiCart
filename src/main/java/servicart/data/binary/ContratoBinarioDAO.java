package servicart.data.binary;

import servicart.models.entities.Contrato;
import servicart.models.enums.CausaTerminacion;

/* Se eliminó terminarContrato(id, causa) que existía antes.
Esa operación es responsabilidad del ContratoService
ContratoService busca el contrato con findId()
Le asigna la causa y la fechaFin
Llama update() para persistir
isActivo filtra contratos terminados de findAll() automáticamente
Para "terminar" un contrato: el servicio llama update(), no delete() */
public class ContratoBinarioDAO extends GenericBinarioDAO<Contrato> {
    public ContratoBinarioDAO() {
        super("bin/contrato.bin"); //Archivo único para esta entidad
    }

    @Override
    protected String getId(Contrato contrato) { return String.valueOf(contrato.getId()); } //Identificador natural

    @Override
    protected boolean isActivo(Contrato contrato) { return contrato.getCausaTerminacion() == CausaTerminacion.ACTIVO; } //Borrado lógico: activo = 1
}
