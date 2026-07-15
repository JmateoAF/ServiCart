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

    public CorteService(CrudDAO<CorteServicio> corteDAO) {
        this.corteDAO = corteDAO;
    }

    // SIN CAMBIOS — el guard ya es correcto y sigue impidiendo el doble corte
    public void cortarServicio(Contrato contrato, Factura factura) {
        if (buscarCorteVigente(contrato.getId()).isPresent()) {
            throw new IllegalStateException("El contrato " + contrato.getId() + " ya tiene un corte vigente");
        }
        CorteServicio corte = new CorteServicio(LocalDateTime.now(), contrato, factura);
        corteDAO.save(corte);
    }

    /* El costo de reactivación no se persiste en ningún campo: se calcula siempre al
       vuelo en FacturacionService (buscarCortePorFactura + estadoCortado), como parte
       del saldo pendiente de la MISMA factura que originó el corte. Por eso se paga
       con el flujo normal de abonos/carrito/checkout, y no hay nada que MoraService
       pueda pisar al recalcular la mora del día siguiente. */
    public void reactivarServicio(CorteServicio corte, double costoPagado) {
        double costoRequerido = corte.getContrato().getServicio().getCostoReactivacion();
        if (costoPagado < costoRequerido - 0.005) {
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

    public Optional<CorteServicio> buscarCortePorFactura(int idFactura) {
        return corteDAO.findAll().stream()
                .filter(c -> c.getFactura().getId() == idFactura)
                .findFirst();
    }

    public List<CorteServicio> buscarCortados() {
        return corteDAO.findAll().stream().filter(CorteServicio::estadoCortado).toList();
    }
}