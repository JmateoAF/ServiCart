package servicart.domain.services.empresa;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.*;
import servicart.entities.enums.EstadoFactura;
import java.time.LocalDateTime;
import java.util.List;

public class FacturacionService {
    private final CrudDAO<Factura> facturaDAO;
    private final CrudDAO<Abono> abonoDAO;

    public FacturacionService(CrudDAO<Factura> facturaDAO, CrudDAO<Abono> abonoDAO) {
        this.facturaDAO = facturaDAO;
        this.abonoDAO = abonoDAO;
    }

    public Factura emitirFactura(Contrato contrato, double consumo) {
        double monto = contrato.getServicio().calcularMonto(consumo);
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime vencimiento = ahora.plusDays(30);
        LocalDateTime corte = ahora.plusDays(45);

        Factura factura = new Factura(ahora, vencimiento, corte, monto, contrato);
        facturaDAO.save(factura);
        return factura;
    }

    public List<Factura> buscarPorContrato(int contratoId) {
        return facturaDAO.findAll().stream().filter(f -> f.getContrato().getId() == contratoId).toList();
    }

    public List<Factura> buscarPendientesPorCliente(String cedula) {
        return facturaDAO.findAll().stream().filter(f -> f.getContrato().getCliente().getCedula().equals(cedula)).filter(f -> f.getEstado() != EstadoFactura.PAGADA).toList();
    }

    public void marcarComoPagada(Factura factura) {
        factura.setEstado(EstadoFactura.PAGADA);
        facturaDAO.update(factura);
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

    /* Lo que realmente falta por pagar de esta factura ahora mismo (no incluye costoReactivacion).
    Efecto colateral intencional: si los abonos ya cubren el total pero el estado guardado
    quedó desactualizado (dato viejo, seed mal alineado, corte de luz a mitad de un checkout...),
    esta lectura autocorrige el estado en vez de devolver un saldo que lo contradice */
    public double calcularSaldoPendiente(Factura factura) {
        double totalReal = factura.getValorBase() + factura.interesAcumulado();
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
}