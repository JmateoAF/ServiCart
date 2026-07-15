package servicart.entities;

import servicart.domain.interfaces.Identificable;

import java.io.Serializable;
import java.time.LocalDateTime;

/* Registro del interés por mora aplicado a una factura vencida.
Los intereses son progresivos según los días de retraso */

public class InteresMora implements Serializable, Identificable {
    private final Factura factura;
    private int id;
    private int diasRetraso;
    private double interesAcumulado;
    private LocalDateTime fechaCalculo;
    private boolean aplicadoAFactura;

    public InteresMora(int diasRetraso, double interesAcumulado, LocalDateTime fechaCalculo, boolean aplicadoAFactura, Factura factura) {
        this.diasRetraso = diasRetraso;
        this.interesAcumulado = interesAcumulado;
        this.fechaCalculo = fechaCalculo;
        this.aplicadoAFactura = aplicadoAFactura;
        this.factura = factura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiasRetraso() {
        return diasRetraso;
    }

    public void setDiasRetraso(int diasRetraso) {
        this.diasRetraso = diasRetraso;
    }

    public double getInteresAcumulado() {
        return interesAcumulado;
    }

    public void setInteresAcumulado(double interesAcumulado) {
        this.interesAcumulado = interesAcumulado;
    }

    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }

    public void setFechaCalculo(LocalDateTime fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
    }

    public boolean isAplicadoAFactura() {
        return aplicadoAFactura;
    }

    public void setAplicadoAFactura(boolean aplicadoAFactura) {
        this.aplicadoAFactura = aplicadoAFactura;
    }

    public Factura getFactura() {
        return factura;
    }
}
