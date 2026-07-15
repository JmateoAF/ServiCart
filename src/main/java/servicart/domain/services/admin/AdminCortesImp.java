package servicart.domain.services.admin;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.ForzarCorteDTOEntrada;
import servicart.domain.dtos.entradas.PagarFacturaDTOEntrada;
import servicart.domain.dtos.entradas.ReactivarCorteDTOEntrada;
import servicart.domain.dtos.retornos.CorteDTORetorno;
import servicart.domain.dtos.retornos.FacturaEnMoraDTORetorno;
import servicart.domain.dtos.retornos.ResumenCortesDTORetorno;
import servicart.domain.interfaces.AdminCortes;
import servicart.domain.mappers.AdminCortesMapperDomain;
import servicart.domain.services.empresa.ContratoService;
import servicart.domain.services.empresa.CorteService;
import servicart.domain.services.empresa.FacturacionService;
import servicart.entities.Abono;
import servicart.entities.Contrato;
import servicart.entities.CorteServicio;
import servicart.entities.Factura;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class AdminCortesImp implements AdminCortes {

    @Override
    public ResumenCortesDTORetorno obtenerResumen() {
        List<Factura> vencidas = facturasVencidas();
        CorteService corteService = crearCorteService();

        int totalCortados = corteService.buscarCortados().size();
        int totalEnMora = (int) vencidas.stream()
                .filter(f -> corteService.buscarCorteVigente(f.getContrato().getId()).isEmpty())
                .count();
        double interesesGenerados = vencidas.stream().mapToDouble(Factura::interesAcumulado).sum();

        return new ResumenCortesDTORetorno(totalCortados, totalEnMora, interesesGenerados);
    }

    @Override
    public List<FacturaEnMoraDTORetorno> listarEnMoraSinCorte() {
        CorteService corteService = crearCorteService();
        FacturacionService facturacionService = crearFacturacionService();

        return facturasVencidas().stream()
                .filter(f -> corteService.buscarCorteVigente(f.getContrato().getId()).isEmpty())
                .map(f -> AdminCortesMapperDomain.enMoraADTO(f, facturacionService.calcularSaldoPendiente(f)))
                .toList();
    }

    @Override
    public List<CorteDTORetorno> listarCortados() {
        FacturacionService facturacionService = crearFacturacionService();

        return crearCorteService().buscarCortados().stream()
                .map(c -> AdminCortesMapperDomain.corteADTO(c, facturacionService.calcularSaldoPendiente(c.getFactura())))
                .toList();
    }

    @Override
    public void forzarCorte(ForzarCorteDTOEntrada dto) {
        ContratoService contratoService = new ContratoService(FactoryDAO.getDAO(Contrato.class));
        FacturacionService facturacionService = crearFacturacionService();
        CorteService corteService = crearCorteService();

        Contrato contrato = contratoService.buscarPorId(String.valueOf(dto.idContrato()))
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado: " + dto.idContrato()));

        Factura factura = facturacionService.buscarPorContrato(contrato.getId()).stream()
                .filter(Factura::estaVencida)
                .max(Comparator.comparingLong(Factura::diasDeRetraso))
                .orElseThrow(() -> new IllegalStateException("El contrato no tiene facturas vencidas"));

        corteService.cortarServicio(contrato, factura);
    }

    @Override
    public void reactivar(ReactivarCorteDTOEntrada dto) {
        CrudDAO<CorteServicio> corteDAO = FactoryDAO.getDAO(CorteServicio.class);
        CorteService corteService = crearCorteService();

        assert corteDAO != null;
        CorteServicio corte = corteDAO.findId(String.valueOf(dto.idCorte()))
                .orElseThrow(() -> new RuntimeException("Corte no encontrado: " + dto.idCorte()));

        corteService.reactivarServicio(corte, dto.montoPagado());
    }

    @Override
    public void pagarFactura(PagarFacturaDTOEntrada dto) {
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);
        CrudDAO<Abono> abonoDAO = FactoryDAO.getDAO(Abono.class);
        FacturacionService facturacionService = crearFacturacionService();

        assert facturaDAO != null;
        Factura factura = facturaDAO.findId(String.valueOf(dto.idFactura()))
                .orElseThrow(() -> new RuntimeException("Factura no encontrada: " + dto.idFactura()));

        double saldoPendiente = facturacionService.calcularSaldoPendiente(factura);
        if (dto.monto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a $0.00");
        }
        if (dto.monto() > saldoPendiente + 0.005) {
            throw new IllegalArgumentException("No puedes pagar más de lo que debe ($" + String.format("%.2f", saldoPendiente) + ")");
        }

        Abono abono = new Abono(dto.monto(), LocalDateTime.now(), true, factura, dto.modalidadPago());
        assert abonoDAO != null;
        abonoDAO.save(abono);

        if (facturacionService.calcularSaldoPendiente(factura) <= 0) {
            facturacionService.marcarComoPagada(factura);
        }
    }

    private List<Factura> facturasVencidas() {
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);
        assert facturaDAO != null;
        return facturaDAO.findAll().stream().filter(Factura::estaVencida).toList();
    }

    private CorteService crearCorteService() {
        return new CorteService(FactoryDAO.getDAO(CorteServicio.class));
    }

    private FacturacionService crearFacturacionService() {
        return new FacturacionService(FactoryDAO.getDAO(Factura.class), FactoryDAO.getDAO(Abono.class), crearCorteService());
    }
}
