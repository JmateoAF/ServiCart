package servicart.data.binary;

import servicart.entities.Factura;

public class FacturaBinarioDAO extends GenericBinarioDAO<Factura> {
    public FacturaBinarioDAO() {super("bin/factura.bin"); } //Archivo único para esta entidad

    @Override
    protected String getId(Factura entidad) { return String.valueOf(entidad.getId()); } // identificador natural
}
