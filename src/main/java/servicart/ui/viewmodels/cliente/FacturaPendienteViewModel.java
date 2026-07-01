package servicart.ui.viewmodels.cliente;

public class FacturaPendienteViewModel {
    private int idFactura;
    private String numeroFactura;
    private String monto;               // "$ 12.50"
    private String fechaVencimiento;    // "15/07/2026"
    private String diasMora;            // "12 días"
    private String interesAcumulado;    // "+ $3.00"

    public FacturaPendienteViewModel() {}

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public String getMonto() { return monto; }
    public void setMonto(String monto) { this.monto = monto; }

    public String getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getDiasMora() { return diasMora; }
    public void setDiasMora(String diasMora) { this.diasMora = diasMora; }

    public String getInteresAcumulado() { return interesAcumulado; }
    public void setInteresAcumulado(String interesAcumulado) { this.interesAcumulado = interesAcumulado; }
}