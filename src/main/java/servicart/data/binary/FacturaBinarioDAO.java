package servicart.data.binary;

import servicart.models.entities.Factura;

/* Se eliminó facturaPagada(id) que existía antes.
Marcar una factura como PAGADA es responsabilidad del FacturacionService:
FacturacionService busca la factura con findId()
Cambia el estado: factura.setEstado(EstadoFactura.PAGADA)
Llama update() para persistir */
public class FacturaBinarioDAO extends GenericBinarioDAO<Factura> {
    public FacturaBinarioDAO() {super("bin/factura.bin"); } //Archivo único para esta entidad

    @Override
    protected String getId(Factura entidad) { return String.valueOf(entidad.getId()); } // identificador natural
}
