package servicart.domain.services.cliente;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.AgregarAbonoDTOEntrada;
import servicart.domain.dtos.entradas.PanelClienteDTOEntrada;
import servicart.domain.dtos.salidas.ServicioContratadoDTOSalida;
import servicart.domain.interfaces.PanelCliente;
import servicart.domain.mappers.PanelClienteMapperDomain;
import servicart.domain.services.ContratoService;
import servicart.domain.services.FacturacionService;
import servicart.entities.*;
import servicart.entities.enums.EstadoFactura;
import servicart.entities.enums.ModalidadPago;

import java.time.LocalDateTime;
import java.util.List;

public class PanelClienteImp implements PanelCliente {

    @Override
    public List<ServicioContratadoDTOSalida> listarServiciosContratados(PanelClienteDTOEntrada dto) {
        ContratoService contratoService = new ContratoService(FactoryDAO.getDAO(Contrato.class));
        FacturacionService facturacionService = new FacturacionService(FactoryDAO.getDAO(Factura.class));
        CrudDAO<InteresMora> interesMoraDAO = FactoryDAO.getDAO(InteresMora.class);
        assert interesMoraDAO != null;
        List<InteresMora> todosLosIntereses = interesMoraDAO.findAll();

        return contratoService.buscarPorCliente(dto.cedula()).stream()
                .map(contrato -> {
                    List<Factura> pendientes = facturacionService.buscarPorContrato(contrato.getId()).stream()
                            .filter(f -> f.getEstado() != EstadoFactura.PAGADA)
                            .sorted((f1, f2) -> f1.getFechaEmision().compareTo(f2.getFechaEmision()))
                            .toList();
                    return PanelClienteMapperDomain.entidadADTO(contrato, pendientes, todosLosIntereses);
                })
                .toList();
    }

    @Override
    public void agregarAbonoAlCarrito(AgregarAbonoDTOEntrada dto) {
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);
        CrudDAO<Abono> abonoDAO = FactoryDAO.getDAO(Abono.class);
        CrudDAO<Cliente> clienteDAO = FactoryDAO.getDAO(Cliente.class);
        CarritoService carritoService = new CarritoService(FactoryDAO.getDAO(Carrito.class));

        assert facturaDAO != null;
        Factura factura = facturaDAO.findId(String.valueOf(dto.idFactura())).orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        assert clienteDAO != null;
        Cliente cliente = clienteDAO.findId(dto.cedula()).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Abono abono = new Abono(dto.monto(), LocalDateTime.now(), false, factura, ModalidadPago.TC);
        assert abonoDAO != null;
        abonoDAO.save(abono);
        carritoService.agregarAbono(cliente, abono);
    }
}