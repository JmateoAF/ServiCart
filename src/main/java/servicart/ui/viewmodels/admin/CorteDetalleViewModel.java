package servicart.ui.viewmodels.admin;

public class CorteDetalleViewModel {
    private int idContrato;
    private int idCorte;   // -1 si no aplica (aún no cortado)
    private int idFactura;
    private boolean cortado;
    private String nombreCliente;
    private String servicio;
    private int diasMora;
    private String deudaOriginal;
    private String interesAcumulado;
    private String costoReactivacion;
    private String total;
    private String pieTexto; //"12/30 días para corte" o "Servicio cortado"
    private double saldoPendiente; // deuda + interés, sin costo de reactivación; para prellenar el diálogo de pago

    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }

    public int getIdCorte() { return idCorte; }
    public void setIdCorte(int idCorte) { this.idCorte = idCorte; }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public double getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(double saldoPendiente) { this.saldoPendiente = saldoPendiente; }

    public boolean isCortado() { return cortado; }
    public void setCortado(boolean cortado) { this.cortado = cortado; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }

    public int getDiasMora() { return diasMora; }
    public void setDiasMora(int diasMora) { this.diasMora = diasMora; }

    public String getDeudaOriginal() { return deudaOriginal; }
    public void setDeudaOriginal(String deudaOriginal) { this.deudaOriginal = deudaOriginal; }

    public String getInteresAcumulado() { return interesAcumulado; }
    public void setInteresAcumulado(String interesAcumulado) { this.interesAcumulado = interesAcumulado; }

    public String getCostoReactivacion() { return costoReactivacion; }
    public void setCostoReactivacion(String costoReactivacion) { this.costoReactivacion = costoReactivacion; }

    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }

    public String getPieTexto() { return pieTexto; }
    public void setPieTexto(String pieTexto) { this.pieTexto = pieTexto; }
}
