package servicart.domain.services.cliente;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.CarritoDTOEntrada;
import servicart.domain.dtos.retornos.CarritoDTORetorno;
import servicart.domain.interfaces.CarritoCliente;
import servicart.domain.mappers.CarritoMapperDomain;
import servicart.entities.Carrito;
import servicart.entities.Cliente;

public class CarritoClienteImp implements CarritoCliente {
    @Override
    public CarritoDTORetorno verCarrito(CarritoDTOEntrada dto) {
        CrudDAO<Cliente> clienteDAO = FactoryDAO.getDAO(Cliente.class);
        CarritoService carritoService = new CarritoService(FactoryDAO.getDAO(Carrito.class));

        assert clienteDAO != null;
        Cliente cliente = clienteDAO.findId(dto.cedula()).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Carrito carrito = carritoService.obtenerOCrearCarrito(cliente);
        return CarritoMapperDomain.entidadADTO(carrito);
    }

    @Override
    public void vaciarCarrito(CarritoDTOEntrada dto) {
        CrudDAO<Cliente> clienteDAO = FactoryDAO.getDAO(Cliente.class);
        CarritoService carritoService = new CarritoService(FactoryDAO.getDAO(Carrito.class));

        assert clienteDAO != null;
        Cliente cliente = clienteDAO.findId(dto.cedula()).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        carritoService.vaciarCarrito(cliente);
    }
}