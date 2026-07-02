package servicart.domain.services;

import servicart.data.interfaces.CrudDAO;
import servicart.ui.dtos.CorteRequestDTO;
import servicart.ui.dtos.ReactivacionRequestDTO;
import servicart.entities.Contrato;
import servicart.entities.CorteServicio;
import servicart.entities.Factura;
import servicart.entities.enums.EstadoCorte;
import servicart.exceptions.EntidadNoEncontradaException;
import servicart.exceptions.ServiCartException;

import java.time.LocalDateTime;
import java.util.Optional;

public class CorteService {
    private final CrudDAO<CorteServicio> corteDAO;
    private final ContratoService contratoService;      // nuevo
    private final FacturacionService facturacionService; // nuevo

    // Constructor ampliado
    public CorteService(CrudDAO<CorteServicio> corteDAO,
                        ContratoService contratoService,
                        FacturacionService facturacionService) {
        this.corteDAO = corteDAO;
        this.contratoService = contratoService;
        this.facturacionService = facturacionService;
    }

    public CorteServicio cortarServicio(Contrato contrato, Factura factura) {
        if (!factura.superaFechaCorte())
            throw new ServiCartException("La factura aún no superó la fecha de corte");
        if (tieneCortePendiente(contrato.getId()))
            throw new ServiCartException("El servicio ya está cortado para este contrato");

        CorteServicio corte = new CorteServicio(LocalDateTime.now(), contrato, factura);
        corteDAO.save(corte);
        return corte;
    }

    //acepta el DTO desde el controlador
    public CorteServicio cortarServicio(CorteRequestDTO dto) {
        // 1. Obtener el contrato
        Contrato contrato = contratoService.buscarPorId(dto.getContratoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Contrato no encontrado"));

        // 2. Obtener la factura que supera la fecha de corte
        Factura factura = facturacionService.buscarPorContrato(contrato.getId()).stream()
                .filter(Factura::superaFechaCorte)
                .findFirst()
                .orElseThrow(() -> new ServiCartException("No hay factura que supere la fecha de corte"));

        // 3. Llamar al metodo original
        CorteServicio corte = cortarServicio(contrato, factura);

        return corte;
    }

    // Metodo original
    public void reactivarServicio(CorteServicio corte, double costoReactivacionPagado) {
        if (!corte.estadoCortado())
            throw new ServiCartException("El servicio no está en estado cortado");
        corte.setFechaReactivacion(LocalDateTime.now());
        corte.setCostoReactivacionPagado(costoReactivacionPagado);
        corte.setEstadoCorte(EstadoCorte.ACTIVO);
        corteDAO.update(corte);
    }

    // acepta el DTO desde el controlador
    public void reactivarServicio(ReactivacionRequestDTO dto) {
        // Buscar el corte por ID
        CorteServicio corte = corteDAO.findId(dto.getCorteId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Corte no encontrado"));
        reactivarServicio(corte, dto.getCostoReactivacionPagado());
    }

    public boolean tieneCortePendiente(int contratoId) {
        return corteDAO.findAll().stream().anyMatch(c -> c.getContrato().getId() == contratoId && c.getEstadoCorte() == EstadoCorte.CORTADO);
    }

    public Optional<CorteServicio> buscarCortePorContrato(int contratoId) {
        return corteDAO.findAll().stream().filter(c -> c.getContrato().getId() == contratoId && c.estadoCortado()).findFirst();
    }
}
