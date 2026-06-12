package servicart.domain.models.entidades;

import servicart.domain.models.enums.EstadoCorte;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CorteServicio implements Serializable {
    private final int id;
    private final LocalDateTime fechaCorte;
    private final LocalDateTime fechaReactivacion;
    private double costoReactivacionPagado;
    private EstadoCorte estadoCorte;
    private final Contrato contrato;
    private final Factura factura;

    public CorteServicio(int id, LocalDateTime fechaCorte, LocalDateTime fechaReactivacion, double costoReactivacionPagado, Contrato contrato, Factura factura) {
        this.id = id;
        this.fechaCorte = fechaCorte;
        this.fechaReactivacion = fechaReactivacion;
        this.costoReactivacionPagado = costoReactivacionPagado;
        setEstadoCorte(EstadoCorte.ACTIVO);
        this.contrato = contrato;
        this.factura = factura;
    }

    public int getId() { return id; }

    public LocalDateTime getFechaCorte() { return fechaCorte; }

    public LocalDateTime getFechaReactivacion() { return fechaReactivacion; }

    public double getCostoReactivacionPagado() { return costoReactivacionPagado; }
    public void setCostoReactivacionPagado(double costoReactivacionPagado) { this.costoReactivacionPagado = costoReactivacionPagado; }

    public EstadoCorte getEstadoCorte() { return estadoCorte; }
    public void setEstadoCorte(EstadoCorte estadoCorte) { this.estadoCorte = estadoCorte; }

    public Contrato getContrato() { return contrato; }

    public Factura getFactura() { return factura; }
}
