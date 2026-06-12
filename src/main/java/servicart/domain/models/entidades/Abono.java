package servicart.domain.models.entidades;

import servicart.domain.models.enums.ModalidadPago;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Abono implements Serializable {
    private final int id;
    private double monto;
    private final LocalDateTime fechaPago;
    private boolean pagoRealizado;
    private final Factura factura;
    private final ModalidadPago modalidadPago;

    public Abono(int id, double monto, LocalDateTime fechaPago, boolean pagoRealizado, Factura factura, ModalidadPago modalidadPago) {
        this.id = id;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.pagoRealizado = pagoRealizado;
        this.factura = factura;
        this.modalidadPago = modalidadPago;
    }

    public int getId() { return id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public LocalDateTime getFechaPago() { return fechaPago; }

    public boolean getPagoRealizado() { return pagoRealizado; }
    public void setPagoRealizado(boolean pagoRealizado) { this.pagoRealizado = pagoRealizado; }

    public Factura getFactura() { return factura; }

    public ModalidadPago getModalidadPago() { return modalidadPago; }
}
