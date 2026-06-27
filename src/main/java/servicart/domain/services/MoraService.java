package servicart.domain.services;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entities.Factura;
import servicart.domain.models.entities.InteresMora;
import servicart.domain.models.enums.EstadoFactura;
import servicart.exceptions.ServiCartException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/* Si se supera la fecha de vencimiento se aplicarán intereses
progresivos sobre el valor de la factura, dependiendo de la
cantidad de días de retraso en el pago
Fórmula: interés = valorTotal × tasaInteresDiario × diasRetraso */

public class MoraService {
    private final CrudDAO<InteresMora> interesMoraDAO;
    private final CrudDAO<Factura> facturaDAO;

    public MoraService(CrudDAO<InteresMora> interesMoraDAO, CrudDAO<Factura> facturaDAO) {
        this.interesMoraDAO = interesMoraDAO;
        this.facturaDAO = facturaDAO;
    }

    public InteresMora aplicarMora(Factura factura) {
        if (factura == null) throw new ServiCartException("La factura no puede ser nula", e);
        if (factura.getEstado() == EstadoFactura.PAGADA) throw new ServiCartException("No se puede aplicar mora a una factura ya pagada", e);
        if (!factura.estaVencida()) throw new ServiCartException("La factura aún no está vencida", e);

        LocalDateTime ahora  = LocalDateTime.now();
        long dias = ChronoUnit.DAYS.between(factura.getFechaVencimiento(), ahora);
        double tasa = factura.getContrato().getServicio().getTasaInteresDiario();
        double interes = factura.getValorTotal() * tasa * dias;

        //Crear y guardar el registro de mora
        InteresMora mora = new InteresMora((int) dias, interes, ahora, false, factura);
        interesMoraDAO.save(mora);

        //Aplicar la mora al total de la factura
        factura.setValorTotal(factura.getValorTotal() + interes);
        facturacionService.marcarComoVencida(factura);
        mora.setAplicadoAFactura(true);

        interesMoraDAO.update(mora);
        facturaDAO.update(factura);

        return mora;
    }

    // Referencia al servicio de facturación para cambiar el estado
    private FacturacionService facturacionService;

    public void setFacturacionService(FacturacionService fs) {
        this.facturacionService = fs;
    }
}
