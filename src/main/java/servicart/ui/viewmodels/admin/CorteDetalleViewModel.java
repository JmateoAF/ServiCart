package servicart.ui.viewmodels.admin;

public class CorteDetalleViewModel {
    private String nombreCliente;
    private String servicio;
    private int diasMora;
    private String deudaOriginal;
    private String interesAcumulado;
    private String costoReactivacion;
    private String estado; //Cortado o en mora
    private String total;
    private double progresoCorte; //0.0 a 1.0 para la barra de progreso

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

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }

    public double getProgresoCorte() { return progresoCorte; }
    public void setProgresoCorte(double progresoCorte) { this.progresoCorte = progresoCorte; }
}