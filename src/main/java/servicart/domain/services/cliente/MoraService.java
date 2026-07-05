package servicart.domain.services.cliente;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.services.FacturacionService;
import servicart.entities.Factura;
import servicart.entities.InteresMora;

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
    /*    if (factura == null)
        if (factura.getEstado() == EstadoFactura.PAGADA)

        if (!factura.estaVencida())
        boolean moraYaAplicada = interesMoraDAO.findAll().stream().anyMatch(m -> m.getFactura().getId() == factura.getId() && m.isAplicadoAFactura());
        if (moraYaAplicada)*/

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
