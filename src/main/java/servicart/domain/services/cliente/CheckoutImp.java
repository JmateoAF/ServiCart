package servicart.domain.services.cliente;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.CarritoDTOEntrada;
import servicart.domain.dtos.entradas.ConfirmarPagoDTOEntrada;
import servicart.domain.dtos.retornos.CarritoDTORetorno;
import servicart.domain.interfaces.Checkout;
import servicart.domain.mappers.CarritoMapperDomain;
import servicart.domain.services.empresa.FacturacionService;
import servicart.domain.services.empresa.NotificadorService;
import servicart.entities.Abono;
import servicart.entities.Carrito;
import servicart.entities.Cliente;
import servicart.entities.Factura;

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

        Cliente cliente = buscarCliente(dto.cedula());
        CarritoService carritoService = new CarritoService(carritoDAO);
        FacturacionService facturacionService = new FacturacionService(facturaDAO, abonoDAO);
        CheckoutService checkoutService = new CheckoutService(abonoDAO, carritoDAO, facturacionService);

        Carrito carrito = carritoService.obtenerOCrearCarrito(cliente);
        if (carrito.estaVacio()) {
            throw new IllegalStateException("El carrito está vacío, no hay nada que pagar");
        }

        // La modalidad de pago se elige aquí en el checkout
        for (Abono abono : carrito.getAbonos()) {
            abono.setModalidadPago(dto.modalidadPago());
        }

        double totalPagado = carrito.getTotal(); // capturarlo antes de procesar, porque procesarPago vacía el carrito
        checkoutService.procesarPago(carrito);

        new NotificadorService().notificarPago(cliente, totalPagado);
    }

    private Cliente buscarCliente(String cedula) {
        CrudDAO<Cliente> clienteDAO = FactoryDAO.getDAO(Cliente.class);
        assert clienteDAO != null;
        return clienteDAO.findId(cedula).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
}