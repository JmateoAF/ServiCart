package servicart.domain.services.cliente;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.AgregarAbonoDTOEntrada;
import servicart.domain.dtos.entradas.PanelClienteDTOEntrada;
import servicart.domain.dtos.retornos.ServicioContratadoDTORetorno;
import servicart.domain.interfaces.PanelCliente;
import servicart.domain.mappers.PanelClienteMapperDomain;
import servicart.domain.services.empresa.ContratoService;
import servicart.domain.services.empresa.CorteService;
import servicart.domain.services.empresa.FacturacionService;
import servicart.entities.*;
import servicart.entities.enums.EstadoFactura;
import servicart.entities.enums.ModalidadPago;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PanelClienteImp implements PanelCliente {

    @Override
    public List<ServicioContratadoDTORetorno> listarServiciosContratados(PanelClienteDTOEntrada dto) {
        ContratoService contratoService = new ContratoService(FactoryDAO.getDAO(Contrato.class));
        FacturacionService facturacionService = new FacturacionService(FactoryDAO.getDAO(Factura.class), FactoryDAO.getDAO(Abono.class));
        CorteService corteService = new CorteService(FactoryDAO.getDAO(CorteServicio.class));

        return contratoService.buscarPorCliente(dto.cedula()).stream()
                .map(contrato -> {
                    List<Factura> pendientes = facturacionService.buscarPorContrato(contrato.getId()).stream()
                            .filter(f -> f.getEstado() != EstadoFactura.PAGADA)
                            .sorted(Comparator.comparing(Factura::getFechaEmision))
                            .toList();

                    Map<Integer, Double> saldosPendientes = pendientes.stream()
                            .collect(Collectors.toMap(Factura::getId, facturacionService::calcularSaldoPendiente));

                    boolean estaCortado = corteService.buscarCorteVigente(contrato.getId()).isPresent();
                    double deudaTotal = saldosPendientes.values().stream().mapToDouble(Double::doubleValue).sum();

                    return PanelClienteMapperDomain.entidadADTO(contrato, pendientes, estaCortado, deudaTotal, saldosPendientes);
                })
                .toList();
    }

    @Override
    public void agregarAbonoAlCarrito(AgregarAbonoDTOEntrada dto) {
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);
        CrudDAO<Abono> abonoDAO = FactoryDAO.getDAO(Abono.class);
        CrudDAO<Cliente> clienteDAO = FactoryDAO.getDAO(Cliente.class);
        CarritoService carritoService = new CarritoService(FactoryDAO.getDAO(Carrito.class));
        FacturacionService facturacionService = new FacturacionService(facturaDAO, abonoDAO);

        if (facturaDAO == null || abonoDAO == null || clienteDAO == null) {
            throw new IllegalStateException("No se pudieron obtener los DAOs requeridos");
        }

        if (dto.monto() <= 0) {
            throw new IllegalArgumentException("El monto del abono debe ser mayor a 0");
        }

        Factura factura = facturaDAO.findId(String.valueOf(dto.idFactura()))
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalStateException("La factura ya está pagada, no admite abonos");
        }

        double saldoPendiente = facturacionService.calcularSaldoPendiente(factura);
        if (dto.monto() > saldoPendiente + 0.005) {
            throw new IllegalArgumentException("No puedes abonar más del saldo pendiente ($ " + String.format("%.2f", saldoPendiente) + ")");
        }

        Cliente cliente = clienteDAO.findId(dto.cedula())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Abono abono = new Abono(dto.monto(), LocalDateTime.now(), false, factura, ModalidadPago.TC);
        abonoDAO.save(abono);
        carritoService.agregarAbono(cliente, abono);
    }
}