package servicart.domain.models.entidades;

import servicart.domain.models.enums.ModalidadPago;

import java.time.LocalDateTime;

public class CorteServicio {
    private final int id;
    private final LocalDateTime diasRetraso;
    private final LocalDateTime fechaReactivacion;
    private double costoReactivacionPagado;
    private String estadoCorte;
    private final Contrato contrato;
    private final Factura factura;

    public CorteServicio(int id, LocalDateTime diasRetraso, LocalDateTime fechaReactivacion, double costoReactivacionPagado, String estadoCorte, Contrato contrato, Factura factura, ModalidadPago modalidadPago) {
        this.id = id;
        this.diasRetraso = diasRetraso;
        this.fechaReactivacion = fechaReactivacion;
        this.costoReactivacionPagado = costoReactivacionPagado;
        this.estadoCorte = estadoCorte;
        this.contrato = contrato;
        this.factura = factura;
    }

    public int getId() { return id; }

    public LocalDateTime getDiasRetraso() { return diasRetraso; }

    public LocalDateTime getFechaReactivacion() { return fechaReactivacion; }

    public double getCostoReactivacionPagado() { return costoReactivacionPagado; }
    public void setCostoReactivacionPagado(double costoReactivacionPagado) { this.costoReactivacionPagado = costoReactivacionPagado; }

    public String getEstadoCorte() { return estadoCorte; }
    public void setEstadoCorte(String estadoCorte) { this.estadoCorte = estadoCorte; }

    public Contrato getContrato() { return contrato; }

    public Factura getFactura() { return factura; }
}
