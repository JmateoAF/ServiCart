package servicart.domain.services.cliente;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.Abono;
import servicart.entities.Carrito;
import servicart.entities.Cliente;

public class CarritoService {
    private final CrudDAO<Carrito> carritoDAO;

    public CarritoService(CrudDAO<Carrito> carritoDAO) {
        this.carritoDAO = carritoDAO;
    }

    public Carrito obtenerOCrearCarrito(Cliente cliente) {
        return carritoDAO.findAll().stream()
                .filter(c -> c.getCliente().getCedula().equals(cliente.getCedula()))
                .findFirst()
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito(cliente);
                    carritoDAO.save(nuevo);
                    return nuevo;
                });
    }

    public void agregarAbono(Cliente cliente, Abono abono) {
        Carrito carrito = obtenerOCrearCarrito(cliente);
        carrito.agregarAbono(abono);
        carritoDAO.update(carrito);
    }

    public void vaciarCarrito(Cliente cliente) {
        Carrito carrito = obtenerOCrearCarrito(cliente);
        carrito.vaciar();
        carritoDAO.update(carrito);
    }
}