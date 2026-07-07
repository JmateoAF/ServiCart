package servicart.ui.viewmodels.admin;

public class ServicioCatalogoViewModel {
    private String nombreServicio;
    private String empresaNombre;
    private String tipoValor;         //Fijo o variable
    private String tarifa;            //Formateada según tipo
    private String interesMora;
    private String diasParaCorte;
    private String costoReactivacion;

    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public String getEmpresaNombre() { return empresaNombre; }
    public void setEmpresaNombre(String empresaNombre) { this.empresaNombre = empresaNombre; }

    public String getTipoValor() { return tipoValor; }
    public void setTipoValor(String tipoValor) { this.tipoValor = tipoValor; }

    public String getTarifa() { return tarifa; }
    public void setTarifa(String tarifa) { this.tarifa = tarifa; }

    public String getInteresMora() { return interesMora; }
    public void setInteresMora(String interesMora) { this.interesMora = interesMora; }

    public String getDiasParaCorte() { return diasParaCorte; }
    public void setDiasParaCorte(String diasParaCorte) { this.diasParaCorte = diasParaCorte; }

    public String getCostoReactivacion() { return costoReactivacion; }
    public void setCostoReactivacion(String costoReactivacion) { this.costoReactivacion = costoReactivacion; }
}