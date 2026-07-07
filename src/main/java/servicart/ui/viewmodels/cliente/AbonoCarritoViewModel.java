package servicart.ui.viewmodels.cliente;

public class AbonoCarritoViewModel {
    private int idAbono;
    private String servicio;
    private String monto;
    private String referenciaFactura;

    public AbonoCarritoViewModel() {}

    public int getIdAbono() { return idAbono; }
    public void setIdAbono(int idAbono) { this.idAbono = idAbono; }

    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }

    public String getMonto() { return monto; }
    public void setMonto(String monto) { this.monto = monto; }

    public String getReferenciaFactura() { return referenciaFactura; }
    public void setReferenciaFactura(String referenciaFactura) { this.referenciaFactura = referenciaFactura; }
}