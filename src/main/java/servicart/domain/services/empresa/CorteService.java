package servicart.domain.services.empresa;

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

    public void cortarServicio(Contrato contrato, Factura factura) {
        // el costo de reactivación ya fue aplicado, no se vuelve a sumar
        if (buscarCorteVigente(contrato.getId()).isPresent()) {
            throw new IllegalStateException("El contrato " + contrato.getId() + " ya tiene un corte vigente");
        }
        CorteServicio corte = new CorteServicio(LocalDateTime.now(), contrato, factura);
        corteDAO.save(corte);

       // aplicarCostoReactivacionAFactura(factura, contrato);
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
