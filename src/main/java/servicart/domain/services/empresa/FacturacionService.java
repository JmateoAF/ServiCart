package servicart.domain.services.empresa;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.*;
import servicart.entities.enums.EstadoFactura;
import java.time.LocalDateTime;
import java.util.List;

public class FacturacionService {
    private final CrudDAO<Factura> facturaDAO;
    private final CrudDAO<Abono> abonoDAO;
    private final CorteService corteService;

    public FacturacionService(CrudDAO<Factura> facturaDAO, CrudDAO<Abono> abonoDAO, CorteService corteService) {
        this.facturaDAO = facturaDAO;
        this.abonoDAO = abonoDAO;
        this.corteService = corteService;
    }

    public Factura emitirFactura(Contrato contrato, double consumo) {
        double monto = contrato.getServicio().calcularMonto(consumo);
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime vencimiento = ahora.plusDays(7);
        LocalDateTime corte = vencimiento.plusDays(contrato.getServicio().getDiasParaCorte());

        Factura factura = new Factura(ahora, vencimiento, corte, monto, contrato);
        facturaDAO.save(factura);
        return factura;
    }

    public List<Factura> buscarPorContrato(int contratoId) {
        return facturaDAO.findAll().stream().filter(f -> f.getContrato().getId() == contratoId).toList();
    }

    public void marcarComoVencida(Factura factura) {
        factura.setEstado(EstadoFactura.VENCIDA);
        facturaDAO.update(factura);
    }

    public void actualizarValorTotal(Factura factura, double nuevoValorTotal) {
        factura.setValorTotal(nuevoValorTotal);
        facturaDAO.update(factura);
    }

    // Suma de abonos ya confirmados (pagoRealizado=true) para esta factura
    public double calcularTotalPagado(Factura factura) {
        return abonoDAO.findAll().stream()
                .filter(a -> a.getFactura().getId() == factura.getId())
                .filter(Abono::isPagoRealizado)
                .mapToDouble(Abono::getMonto)
                .sum();
    }
    public void marcarComoPagada(Factura factura) {
        factura.setEstado(EstadoFactura.PAGADA);
        facturaDAO.update(factura);
        reactivarCorteSiCorresponde(factura); // NUEVO — cierra el ciclo del checkout
    }

    // Si esta factura es la que provocó un corte todavía vigente, al quedar pagada
    // se reactiva el mismo corte con el costo ya cobrado dentro de esa factura.
    private void reactivarCorteSiCorresponde(Factura factura) {
        corteService.buscarCortePorFactura(factura.getId())
                .filter(CorteServicio::estadoCortado)
                .ifPresent(corte -> {
                    double costoReactivacion = corte.getContrato().getServicio().getCostoReactivacion();
                    corteService.reactivarServicio(corte, costoReactivacion);
                });
    }

    public double calcularSaldoPendiente(Factura factura) {
        double totalReal = calcularTotalReal(factura);
        double totalPagado = calcularTotalPagado(factura);
        double saldo = Math.max(0, totalReal - totalPagado);

        boolean cubierta = totalReal > 0 && totalPagado >= totalReal;
        if (cubierta && factura.getEstado() != EstadoFactura.PAGADA) {
            System.out.println("[Consistencia] Factura " + factura.getId() + " ya estaba cubierta por sus abonos ($"
                    + totalPagado + " de $" + totalReal + ") pero su estado guardado era " + factura.getEstado()
                    + " — se corrige a PAGADA.");
            marcarComoPagada(factura);
        }

        return saldo;
    }

    public double calcularTotalReal(Factura factura) {
        return factura.getValorBase() + factura.interesAcumulado() + costoReactivacionPendiente(factura);
    }

    private double costoReactivacionPendiente(Factura factura) {
        return corteService.buscarCortePorFactura(factura.getId())
                .filter(CorteServicio::estadoCortado)
                .map(corte -> corte.getContrato().getServicio().getCostoReactivacion())
                .orElse(0.0);
    }

}