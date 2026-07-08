package servicart.domain.services.cliente;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.services.empresa.FacturacionService;
import servicart.entities.Abono;
import servicart.entities.Carrito;
import servicart.entities.Factura;
import java.util.List;

/* Válida que el carrito no esté vacío
Marca cada abono como pagoRealizado = true y lo persiste
Si la suma de abonos cubre el total de la factura -> la marca PAGADA
Vacía el carrito */

public class CheckoutService {
    private final CrudDAO<Abono> abonoDAO;
    private final FacturacionService facturacionService;

    public CheckoutService(CrudDAO<Abono> abonoDAO, FacturacionService facturacionService) {
        this.abonoDAO = abonoDAO;
        this.facturacionService = facturacionService;
    }

    public void procesarPago(Carrito carrito) {
        for (Abono abono : carrito.getAbonos()) {
            abono.setPagoRealizado(true);
            abonoDAO.save(abono);

            // Verificar si la factura queda saldada
            Factura factura = abono.getFactura();
            double  totalPagado = calcularTotalPagado(factura);

            if (totalPagado >= factura.getValorTotal()) facturacionService.marcarComoPagada(factura);
        }

        carrito.vaciar();
    }

    //Suma de todos los abonos realizados sobre una misma factura
    private double calcularTotalPagado(Factura factura) {
        List<Abono> abonos = abonoDAO.findAll();

        return abonos.stream().filter(a -> a.getFactura().getId() == factura.getId()).filter(Abono::isPagoRealizado).mapToDouble(Abono::getMonto).sum();
    }
}
