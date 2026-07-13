package servicart.domain.services.cliente;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.services.empresa.FacturacionService;
import servicart.entities.Abono;
import servicart.entities.Carrito;
import servicart.entities.Factura;

public class CheckoutService {
    private final CrudDAO<Abono> abonoDAO;
    private final CrudDAO<Carrito> carritoDAO;
    private final FacturacionService facturacionService;

    public CheckoutService(CrudDAO<Abono> abonoDAO, CrudDAO<Carrito> carritoDAO, FacturacionService facturacionService) {
        this.abonoDAO = abonoDAO;
        this.carritoDAO = carritoDAO;
        this.facturacionService = facturacionService;
    }

    public void procesarPago(Carrito carrito) {
        for (Abono abono : carrito.getAbonos()) {
            abono.setPagoRealizado(true);
            abonoDAO.update(abono);

            Factura factura = abono.getFactura();

            if (facturacionService.calcularSaldoPendiente(factura) <= 0) {
                facturacionService.marcarComoPagada(factura);
            } else {
                facturacionService.actualizarValorTotal(factura, factura.getValorBase() + factura.interesAcumulado());
            }
        }

        carrito.vaciar();
        carritoDAO.update(carrito);
    }
}