package servicart.entities;

import servicart.domain.interfaces.Identificable;
import servicart.entities.enums.EstadoFactura;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Factura implements Serializable, Identificable {
    private int id;
    private final LocalDateTime fechaEmision;
    private final LocalDateTime fechaVencimiento;
    private final LocalDateTime fechaCorte;
    private final double valorBase;   // monto original del servicio, nunca cambia
    private double valorTotal;
    private EstadoFactura estado; // Pagada, vencida, pendiente
    private final Contrato contrato;


    public Factura(LocalDateTime fechaEmision, LocalDateTime fechaVencimiento, LocalDateTime fechaCorte, double valorBase, Contrato contrato) {
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaCorte = fechaCorte;
        this.valorBase = valorBase;
        this.valorTotal = valorBase;
        this.contrato = contrato;
        this.estado = EstadoFactura.PENDIENTE;
    }
    public long diasDeRetraso() {
        if (!estaVencida()) return 0;
        return ChronoUnit.DAYS.between(fechaVencimiento, LocalDateTime.now());
    }

    public double interesAcumulado() {
        if (!estaVencida()) return 0.0;
        double tasa = contrato.getServicio().getTasaInteresDiario();
        return valorBase * tasa * diasDeRetraso(); // sobre valorBase, igual que MoraService
    }

    public int getId() { return id; }
    public void setId(int id) {this.id = id;}

    public LocalDateTime getFechaEmision() { return fechaEmision; }

    public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }

    public LocalDateTime getFechaCorte() { return fechaCorte; }

    public double getValorBase() { return valorBase; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public Contrato getContrato() { return contrato; }

    public boolean estaVencida() { return estado != EstadoFactura.PAGADA && LocalDateTime.now().isAfter(fechaVencimiento); }

    public boolean superaFechaCorte() { return estado != EstadoFactura.PAGADA && LocalDateTime.now().isAfter(fechaCorte); }

    public boolean estaSaldada() { return estado == EstadoFactura.PAGADA; }
}
