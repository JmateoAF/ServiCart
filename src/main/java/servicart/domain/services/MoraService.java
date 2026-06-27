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
    private final FacturacionService facturaService;

    public MoraService(CrudDAO<InteresMora> interesMoraDAO, CrudDAO<Factura> facturaDAO, FacturacionService facturaService) {
        this.interesMoraDAO = interesMoraDAO;
        this.facturaDAO = facturaDAO;
        this.facturaService = facturaService;
    }

    public InteresMora aplicarMora(Factura factura) {
        if (factura == null) throw new ServiCartException("La factura no puede ser nula");
        if (factura.getEstado() == EstadoFactura.PAGADA)
            throw new ServiCartException("No se puede aplicar mora a una factura ya pagada");
        if (!factura.estaVencida()) throw new ServiCartException("La factura aún no está vencida");
        boolean moraYaAplicada = interesMoraDAO.findAll().stream().anyMatch(m -> m.getFactura().getId() == factura.getId() && m.isAplicadoAFactura());
        if (moraYaAplicada) throw new ServiCartException("Ya existe una mora aplicada a esta factura");

        LocalDateTime ahora  = LocalDateTime.now();
        long dias = ChronoUnit.DAYS.between(factura.getFechaVencimiento(), ahora);
        double tasa = factura.getContrato().getServicio().getTasaInteresDiario();
        double interes = factura.getValorTotal() * tasa * dias;

        //Crear y guardar el registro de mora
        InteresMora mora = new InteresMora((int) dias, interes, ahora, false, factura);
        interesMoraDAO.save(mora);

        //Aplicar la mora al total de la factura
        factura.setValorTotal(factura.getValorTotal() + interes);
        facturaService.marcarComoVencida(factura);
        mora.setAplicadoAFactura(true);

        interesMoraDAO.update(mora);
        facturaDAO.update(factura);

        return mora;
    }
}
