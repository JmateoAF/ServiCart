package servicart.data.binary;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entidades.Factura;
import servicart.domain.models.enums.EstadoFactura;

import java.util.List;

public class FacturaBinarioDAO extends GenericBinarioDAO<Factura> implements CrudDAO<Factura>  {
    public FacturaBinarioDAO() {super("bin/factura.bin");               // archivo único para esta entidad
    }

    @Override
    protected String getId(Factura entidad) {
        return String.valueOf(entidad.getId());          // identificador natural
    }

    public void facturaPagada(String id) {
        List<Factura> lista = (cache != null) ? cache : leerTodos();
        for (Factura c : lista) {
            if (getId(c).equals(id)) {
                if (c.getEstado()== EstadoFactura.PAGADA) {          // ya pagada
                    cache = lista;
                    return;
                }
                c.setEstado(EstadoFactura.PAGADA);             // marcado lógico
                guardarTodos(lista);
                cache = lista;
                return;
            }
        }
        throw new RuntimeException("Factura con ID " + id + " no encontrado");
    }
}
