package servicart.domain.services.cliente;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.services.empresa.FacturacionService;
import servicart.entities.Abono;
import servicart.entities.Carrito;
import servicart.entities.Factura;
import java.util.List;

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
            double totalReal = factura.getValorBase() + factura.interesAcumulado();
            double totalPagado = calcularTotalPagado(factura);

            if (totalPagado >= totalReal) {
                facturacionService.marcarComoPagada(factura);
            } else {
                facturacionService.actualizarValorTotal(factura, totalReal);
            }
        }

        carrito.vaciar();
        carritoDAO.update(carrito);
    }

    private double calcularTotalPagado(Factura factura) {
        List<Abono> abonos = abonoDAO.findAll();
        return abonos.stream().filter(a -> a.getFactura().getId() == factura.getId()).filter(Abono::isPagoRealizado).mapToDouble(Abono::getMonto).sum();
    }
}