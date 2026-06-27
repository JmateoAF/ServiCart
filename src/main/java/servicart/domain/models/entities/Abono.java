package servicart.domain.models.entities;

import servicart.domain.interfaces.Identificable;
import servicart.domain.models.enums.ModalidadPago;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Abono implements Serializable, Identificable {
    private int id;
    private final double monto;
    private final LocalDateTime fechaPago;
    private boolean pagoRealizado;
    private final Factura factura;
    private final ModalidadPago modalidadPago;

    public Abono(double monto, LocalDateTime fechaPago, boolean pagoRealizado, Factura factura, ModalidadPago modalidadPago) {
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.pagoRealizado = pagoRealizado;
        this.factura = factura;
        this.modalidadPago = modalidadPago;
    }

    public int getId() { return id; }
    public void setId(int id) {this.id = id;}

    public double getMonto() { return monto; }

    public LocalDateTime getFechaPago() { return fechaPago; }

    public boolean isPagoRealizado() { return pagoRealizado; }
    public void setPagoRealizado(boolean pagoRealizado) { this.pagoRealizado = pagoRealizado; }

    public Factura getFactura() { return factura; }

    public ModalidadPago getModalidadPago() { return modalidadPago; }

    public String getDescripcionServicio() { return factura.getContrato().getServicio().getEmpresa().name() + " – " + factura.getContrato().getServicio().getTipo().name(); }
}
