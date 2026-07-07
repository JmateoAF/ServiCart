package servicart.domain.services;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.Contrato;
import servicart.entities.CorteServicio;
import servicart.entities.Factura;
import servicart.entities.enums.EstadoCorte;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CorteService {
    private final CrudDAO<CorteServicio> corteDAO;
    private final CrudDAO<Factura> facturaDAO; // nuevo: necesita persistir el efecto sobre la factura

    public CorteService(CrudDAO<CorteServicio> corteDAO, CrudDAO<Factura> facturaDAO) {
        this.corteDAO = corteDAO;
        this.facturaDAO = facturaDAO;
    }
    public CorteServicio cortarServicio(Contrato contrato, Factura factura) {
        // el costo de reactivación ya fue aplicado, no se vuelve a sumar
        if (buscarCorteVigente(contrato.getId()).isPresent()) {
            throw new IllegalStateException("El contrato " + contrato.getId() + " ya tiene un corte vigente");
        }
        CorteServicio corte = new CorteServicio(LocalDateTime.now(), contrato, factura);
        corteDAO.save(corte);

        aplicarCostoReactivacionAFactura(factura, contrato);
        return corte;
    }
    private void aplicarCostoReactivacionAFactura(Factura factura, Contrato contrato) {
        double costoReactivacion = contrato.getServicio().getCostoReactivacion();
        // Se suma sobre el valorTotal actual (que ya puede incluir interés de MoraService),
        // nunca sobre valorBase, para no perder el interés ya acumulado
        factura.setValorTotal(factura.getValorTotal() + costoReactivacion);
        facturaDAO.update(factura);
    }
    // Para cuando el cliente pague el costo de reactivación (flujo de checkout, no automático)
    public void reactivarServicio(CorteServicio corte, double costoPagado) {
        double costoRequerido = corte.getContrato().getServicio().getCostoReactivacion();
        if (costoPagado < costoRequerido) {
            throw new IllegalArgumentException("El costo de reactivación debe ser al menos $" + costoRequerido);
        }
        corte.setEstadoCorte(EstadoCorte.ACTIVO);
        corte.setFechaReactivacion(LocalDateTime.now());
        corte.setCostoReactivacionPagado(costoPagado);
        corteDAO.update(corte);
    }

    public List<CorteServicio> buscarPorContrato(int idContrato) {
        return corteDAO.findAll().stream().filter(c -> c.getContrato().getId() == idContrato).toList();
    }

    public Optional<CorteServicio> buscarCorteVigente(int idContrato) {
        return buscarPorContrato(idContrato).stream().filter(CorteServicio::estadoCortado).findFirst();
    }

    public List<CorteServicio> buscarCortados() {
        return corteDAO.findAll().stream().filter(CorteServicio::estadoCortado).toList();
    }
}
/*
package servicart.domain.services;


import servicart.data.interfaces.CrudDAO;
import servicart.domain.services.cliente.ContratoClienteImp;
import servicart.entities.Contrato;
import servicart.entities.CorteServicio;
import servicart.entities.Factura;
import servicart.entities.enums.EstadoCorte;

import java.time.LocalDateTime;
import java.util.Optional;

public class CorteService {
    private final CrudDAO<CorteServicio> corteDAO;
    private final ContratoClienteImp contratoClienteImp;      // nuevo
    private final FacturacionService facturacionService; // nuevo

    // Constructor ampliado
    public CorteService(CrudDAO<CorteServicio> corteDAO,
                        ContratoClienteImp contratoClienteImp,
                        FacturacionService facturacionService) {
        this.corteDAO = corteDAO;
        this.contratoClienteImp = contratoClienteImp;
        this.facturacionService = facturacionService;
    }

    public CorteServicio cortarServicio(Contrato contrato, Factura factura) {
        CorteServicio corte = new CorteServicio(LocalDateTime.now(), contrato, factura);
        corteDAO.save(corte);
        return corte;
    }

    //acepta el DTO desde el controlador
/*    public CorteServicio cortarServicio(CorteRequestDTO dto) {
        // 1. Obtener el contrato
        Contrato contrato = contratoService.buscarPorId(dto.getContratoId());

        // 2. Obtener la factura que supera la fecha de corte
        //Factura factura = facturacionService.buscarPorContrato(contrato.getId()).stream().filter(Factura::superaFechaCorte);

        // 3. Llamar al metodo original
        //CorteServicio corte = cortarServicio(contrato, factura);

        return corte;
    }*/
/*
    // Metodo original
    public void reactivarServicio(CorteServicio corte, double costoReactivacionPagado) {
        corte.setFechaReactivacion(LocalDateTime.now());
        corte.setCostoReactivacionPagado(costoReactivacionPagado);
        corte.setEstadoCorte(EstadoCorte.ACTIVO);
        corteDAO.update(corte);
    }
*/
    // acepta el DTO desde el controlador
/*    public void reactivarServicio(ReactivacionRequestDTO dto) {
        // Buscar el corte por ID
        CorteServicio corte = corteDAO.findId(dto.getCorteId());
        reactivarServicio(corte, dto.getCostoReactivacionPagado());
    }

    public boolean tieneCortePendiente(int contratoId) {
        return corteDAO.findAll().stream().anyMatch(c -> c.getContrato().getId() == contratoId && c.getEstadoCorte() == EstadoCorte.CORTADO);
    }

    public Optional<CorteServicio> buscarCortePorContrato(int contratoId) {
        return corteDAO.findAll().stream().filter(c -> c.getContrato().getId() == contratoId && c.estadoCortado()).findFirst();
    }
}

*/