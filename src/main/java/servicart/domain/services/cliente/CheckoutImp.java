package servicart.domain.services.cliente;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.CarritoDTOEntrada;
import servicart.domain.dtos.entradas.ConfirmarPagoDTOEntrada;
import servicart.domain.dtos.retornos.CarritoDTORetorno;
import servicart.domain.interfaces.Checkout;
import servicart.domain.mappers.CarritoMapperDomain;
import servicart.domain.services.empresa.CorteService;
import servicart.domain.services.empresa.FacturacionService;
import servicart.domain.services.empresa.NotificadorService;
import servicart.entities.*;

import java.util.Map;
import java.util.stream.Collectors;

public class CheckoutImp implements Checkout {
    @Override
    public CarritoDTORetorno obtenerResumen(CarritoDTOEntrada dto) {
        Cliente cliente = buscarCliente(dto.cedula());
        CarritoService carritoService = new CarritoService(FactoryDAO.getDAO(Carrito.class));
        Carrito carrito = carritoService.obtenerOCrearCarrito(cliente);
        return CarritoMapperDomain.entidadADTO(carrito);
    }

    @Override
    public void confirmarPago(ConfirmarPagoDTOEntrada dto) {
        CrudDAO<Carrito> carritoDAO = FactoryDAO.getDAO(Carrito.class);
        CrudDAO<Abono> abonoDAO = FactoryDAO.getDAO(Abono.class);
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);
        CrudDAO<CorteServicio> corteDAO = FactoryDAO.getDAO(CorteServicio.class);

        Cliente cliente = buscarCliente(dto.cedula());
        CarritoService carritoService = new CarritoService(carritoDAO);
        CorteService corteService = new CorteService(corteDAO);
        FacturacionService facturacionService = new FacturacionService(facturaDAO, abonoDAO, corteService);
        CheckoutService checkoutService = new CheckoutService(abonoDAO, carritoDAO, facturacionService);

        Carrito carrito = carritoService.obtenerOCrearCarrito(cliente);
        if (carrito.estaVacio()) {
            throw new IllegalStateException("El carrito está vacío, no hay nada que pagar");
        }

        // La modalidad de pago se elige aquí en el checkout
        for (Abono abono : carrito.getAbonos()) {
            abono.setModalidadPago(dto.modalidadPago());
        }

        validarReactivacionNoSorpresiva(carrito, facturacionService);

        double totalPagado = carrito.getTotal(); // capturarlo antes de procesar, porque procesarPago vacía el carrito
        checkoutService.procesarPago(carrito);

        new NotificadorService().notificarPago(cliente, totalPagado);
    }

    /* Si el carrito se armó cuando el monto puesto por el cliente cubría TODA la
       deuda de esa factura (base + mora) y, mientras tanto, apareció un corte con
       costo de reactivación sobre la misma factura, el pago quedaría corto sin que
       el cliente se entere: la factura no se marca PAGADA, el servicio sigue cortado.
       Esto NO bloquea pagos parciales genuinos — solo el caso puntual en que el
       monto ya alcanzaba para todo menos para una reactivación que apareció después. */
    private void validarReactivacionNoSorpresiva(Carrito carrito, FacturacionService facturacionService) {
        Map<Integer, Double> montoPorFactura = carrito.getAbonos().stream()
                .collect(Collectors.groupingBy(a -> a.getFactura().getId(), Collectors.summingDouble(Abono::getMonto)));

        for (Abono abono : carrito.getAbonos()) {
            Factura factura = abono.getFactura();
            double montoDestinado = montoPorFactura.get(factura.getId());
            double sinReactivacion = factura.getValorBase() + factura.interesAcumulado();
            double conReactivacion = facturacionService.calcularSaldoPendiente(factura);

            boolean cubriaTodoMenosReactivacion = montoDestinado + 0.005 >= sinReactivacion
                    && montoDestinado + 0.005 < conReactivacion;

            if (cubriaTodoMenosReactivacion) {
                double faltante = conReactivacion - montoDestinado;
                throw new IllegalStateException(
                        "El servicio de la factura #" + factura.getId() + " tiene un costo de reactivación " +
                                "pendiente que no estaba reflejado cuando armaste el carrito. Faltan $" +
                                String.format("%.2f", faltante) + " para saldarla y reactivar el servicio. " +
                                "Revisa el carrito y vuelve a intentar.");
            }
        }
    }

    private Cliente buscarCliente(String cedula) {
        CrudDAO<Cliente> clienteDAO = FactoryDAO.getDAO(Cliente.class);
        assert clienteDAO != null;
        return clienteDAO.findId(cedula).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
}