package servicart.domain.models.entidades;

import java.time.LocalDateTime;

public class Factura {
    private final int id;
    private final LocalDateTime fechaEmision;
    private final LocalDateTime fechaVencimiento;
    private final LocalDateTime fechaCorte;
    private double valorTotal;
    private String estado;
    private final Contrato contrato;

    public Factura(int id, LocalDateTime fechaEmision, LocalDateTime fechaVencimiento, LocalDateTime fechaCorte, double valorTotal, String estado, Contrato contrato) {
        this.id = id;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaCorte = fechaCorte;
        this.valorTotal = valorTotal;
        this.estado = estado;
        this.contrato = contrato;
    }

    public int getId() { return id; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }

    public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }

    public LocalDateTime getFechaCorte() { return fechaCorte; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Contrato getContrato() { return contrato; }
}
