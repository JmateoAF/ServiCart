package servicart.domain.models.entities;

import servicart.domain.models.enums.EstadoFactura;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Factura implements Serializable, Identificable {
    private int id;
    private final LocalDateTime fechaEmision;
    private final LocalDateTime fechaVencimiento;
    private final LocalDateTime fechaCorte;
    private double valorTotal;
    private EstadoFactura estado;
    private final Contrato contrato;


    public Factura( LocalDateTime fechaEmision, LocalDateTime fechaVencimiento, LocalDateTime fechaCorte, double valorTotal, Contrato contrato) {
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaCorte = fechaCorte;
        this.valorTotal = valorTotal;
        this.contrato = contrato;
        this.estado = EstadoFactura.PENDIENTE;
    }

    public int getId() { return id; }
    public void setId(int id) {this.id = id;}

    public LocalDateTime getFechaEmision() { return fechaEmision; }

    public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }

    public LocalDateTime getFechaCorte() { return fechaCorte; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public Contrato getContrato() { return contrato; }

    public boolean estaVencida() { return estado != EstadoFactura.PAGADA && LocalDateTime.now().isAfter(fechaVencimiento); }

    public boolean superaFechaCorte() { return estado != EstadoFactura.PAGADA && LocalDateTime.now().isAfter(fechaCorte); }

    public boolean estaSaldada() { return estado == EstadoFactura.PAGADA; }
}
