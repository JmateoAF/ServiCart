package servicart.ui.viewmodels.cliente;

public class FacturaPendienteViewModel {
    private int idFactura;
    private double valorBase;
    private double valorTotal;
    private String periodoTexto;
    private String fechaVencimientoTexto;
    private String fechaCorteTexto;
    private double interesAcumulado;
    private boolean tieneMora;
    private String totalIndividualTexto;

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public double getValorBase() { return valorBase; }
    public void setValorBase(double valorBase) { this.valorBase = valorBase; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    public String getPeriodoTexto() { return periodoTexto; }
    public void setPeriodoTexto(String periodoTexto) { this.periodoTexto = periodoTexto; }

    public String getFechaVencimientoTexto() { return fechaVencimientoTexto; }
    public void setFechaVencimientoTexto(String fechaVencimientoTexto) { this.fechaVencimientoTexto = fechaVencimientoTexto; }

    public String getFechaCorteTexto() { return fechaCorteTexto; }
    public void setFechaCorteTexto(String fechaCorteTexto) { this.fechaCorteTexto = fechaCorteTexto; }

    public double getInteresAcumulado() { return interesAcumulado; }
    public void setInteresAcumulado(double interesAcumulado) { this.interesAcumulado = interesAcumulado; }

    public boolean isTieneMora() { return tieneMora; }
    public void setTieneMora(boolean tieneMora) { this.tieneMora = tieneMora; }

    public String getTotalIndividualTexto() { return totalIndividualTexto; }
    public void setTotalIndividualTexto(String totalIndividualTexto) { this.totalIndividualTexto = totalIndividualTexto; }
}