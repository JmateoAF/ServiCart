package servicart.domain.models.entities;

import servicart.domain.interfaces.Identificable;
import servicart.domain.models.enums.EstadoCorte;
import java.io.Serializable;
import java.time.LocalDateTime;

public class CorteServicio implements Serializable, Identificable {
    private int id;
    private final LocalDateTime fechaCorte;
    private LocalDateTime fechaReactivacion; //Null hasta que se reactive
    private double costoReactivacionPagado;
    private EstadoCorte estadoCorte;
    private final Contrato contrato;
    private final Factura factura;

    public CorteServicio(LocalDateTime fechaCorte, Contrato contrato, Factura factura) {
        this.fechaCorte = fechaCorte;
        this.fechaReactivacion = null;
        this.costoReactivacionPagado = 0.0;
        this.estadoCorte = EstadoCorte.CORTADO;
        this.contrato = contrato;
        this.factura = factura;
    }

    public int getId() { return id; }
    public void setId(int id) {this.id = id;}

    public LocalDateTime getFechaCorte() { return fechaCorte; }

    public LocalDateTime getFechaReactivacion() { return fechaReactivacion; }
    public void setFechaReactivacion(LocalDateTime fechaReactivacion) { this.fechaReactivacion = fechaReactivacion; }

    public double getCostoReactivacionPagado() { return costoReactivacionPagado; }
    public void setCostoReactivacionPagado(double costoReactivacionPagado) { this.costoReactivacionPagado = costoReactivacionPagado; }

    public EstadoCorte getEstadoCorte() { return estadoCorte; }
    public void setEstadoCorte(EstadoCorte estadoCorte) { this.estadoCorte = estadoCorte; }

    public Contrato getContrato() { return contrato; }

    public Factura getFactura() { return factura; }

    public boolean estadoCortado() { return estadoCorte == EstadoCorte.CORTADO; }
}
