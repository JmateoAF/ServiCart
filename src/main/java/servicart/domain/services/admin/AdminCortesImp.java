package servicart.domain.services.admin;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.ForzarCorteDTOEntrada;
import servicart.domain.dtos.entradas.ReactivarCorteDTOEntrada;
import servicart.domain.dtos.retornos.CorteDTORetorno;
import servicart.domain.dtos.retornos.FacturaEnMoraDTORetorno;
import servicart.domain.dtos.retornos.ResumenCortesDTORetorno;
import servicart.domain.interfaces.AdminCortes;
import servicart.domain.mappers.AdminCortesMapperDomain;
import servicart.domain.services.empresa.ContratoService;
import servicart.domain.services.empresa.CorteService;
import servicart.domain.services.empresa.FacturacionService;
import servicart.entities.*;

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

        return facturasVencidas().stream()
                .filter(f -> corteService.buscarCorteVigente(f.getContrato().getId()).isEmpty())
                .map(AdminCortesMapperDomain::enMoraADTO)
                .toList();
    }

    @Override
    public List<CorteDTORetorno> listarCortados() {
        return crearCorteService().buscarCortados().stream()
                .map(AdminCortesMapperDomain::corteADTO)
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

        CorteServicio corte = corteDAO.findId(String.valueOf(dto.idCorte()))
                .orElseThrow(() -> new RuntimeException("Corte no encontrado: " + dto.idCorte()));

        corteService.reactivarServicio(corte, dto.montoPagado());
    }

    private List<Factura> facturasVencidas() {
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);
        return facturaDAO.findAll().stream().filter(Factura::estaVencida).toList();
    }

    private CorteService crearCorteService() {
        return new CorteService(FactoryDAO.getDAO(CorteServicio.class));
    }

    private FacturacionService crearFacturacionService() {
        return new FacturacionService(FactoryDAO.getDAO(Factura.class), FactoryDAO.getDAO(Abono.class));
    }
}
