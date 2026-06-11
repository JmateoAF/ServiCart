package servicart.domain.models.entidades;

import java.time.LocalDateTime;

public class InteresMora {
    private final int id;
    private int diasRetraso;
    private double interesAcumulado;
    private final LocalDateTime fechaCalculo;
    private boolean aplicadoAFactura;
    private final Factura factura;

    public InteresMora(int id, int diasRetraso, double interesAcumulado, LocalDateTime fechaCalculo, boolean aplicadoAFactura, Factura factura) {
        this.id = id;
        this.diasRetraso = diasRetraso;
        this.interesAcumulado = interesAcumulado;
        this.fechaCalculo = fechaCalculo;
        this.aplicadoAFactura = aplicadoAFactura;
        this.factura = factura;
    }

    public int getId() { return id; }

    public int getDiasRetraso() { return diasRetraso; }
    public void setDiasRetraso(int diasRetraso) { this.diasRetraso = diasRetraso; }

    public double getInteresAcumulado() { return interesAcumulado; }
    public void setInteresAcumulado(double interesAcumulado) { this.interesAcumulado = interesAcumulado; }

    public LocalDateTime getFechaCalculo() { return fechaCalculo; }

    public boolean getAplicadoAFactura() { return aplicadoAFactura; }
    public void setAplicadoAFactura(boolean aplicadoAFactura) { this.aplicadoAFactura = aplicadoAFactura; }

    public Factura getFactura() { return factura; }
}
