package servicart.domain.services;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entities.Contrato;
import servicart.domain.models.entities.CorteServicio;
import servicart.domain.models.entities.Factura;
import servicart.domain.models.enums.EstadoCorte;
import servicart.exceptions.ServiCartException;

import java.time.LocalDateTime;

/* Sí se supera la fecha de corte -> cortar el servicio.
Para reactivar -> el cliente paga el costo de reactivación.
Si el corte se prolonga demasiado -> el ContratoService
puede terminar el contrato por causa EMPRESA */

public class CorteService {
    private final CrudDAO<CorteServicio> corteDAO;

    public CorteService(CrudDAO<CorteServicio> corteDAO) {
        this.corteDAO = corteDAO;
    }

    public CorteServicio cortarServicio(Contrato contrato, Factura factura) {
        if (!factura.superaFechaCorte()) throw new ServiCartException("La factura aún no superó la fecha de corte");

        CorteServicio corte = new CorteServicio(LocalDateTime.now(), contrato, factura);
        corteDAO.save(corte);

        return corte;
    }

    public void reactivarServicio(CorteServicio corte, double costoReactivacionPagado) {
        if (!corte.estadoCortado()) throw new ServiCartException("El servicio no está en estado cortado");

        corte.setFechaReactivacion(LocalDateTime.now());
        corte.setCostoReactivacionPagado(costoReactivacionPagado);
        corte.setEstadoCorte(EstadoCorte.ACTIVO);
        corteDAO.update(corte);
    }

    public boolean tieneCortePendiente(int contratoId) {
        return corteDAO.findAll().stream().anyMatch(c -> c.getContrato().getId() == contratoId && c.getEstadoCorte() == EstadoCorte.CORTADO);
    }
}
