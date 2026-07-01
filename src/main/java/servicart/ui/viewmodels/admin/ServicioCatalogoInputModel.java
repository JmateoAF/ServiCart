package servicart.ui.viewmodels.admin;

public class ServicioCatalogoInputModel {
    private String tarifaFija;
    private String tarifaPorUnidad;
    private String interesMoraDiario;
    private String diasParaCorte;        // Este campo podría ser solo de presentación si no existe en la entidad
    private String costoReactivacion;

    public String getTarifaFija() { return tarifaFija; }
    public void setTarifaFija(String tarifaFija) { this.tarifaFija = tarifaFija; }

    public String getTarifaPorUnidad() { return tarifaPorUnidad; }
    public void setTarifaPorUnidad(String tarifaPorUnidad) { this.tarifaPorUnidad = tarifaPorUnidad; }

    public String getInteresMoraDiario() { return interesMoraDiario; }
    public void setInteresMoraDiario(String interesMoraDiario) { this.interesMoraDiario = interesMoraDiario; }

    public String getDiasParaCorte() { return diasParaCorte; }
    public void setDiasParaCorte(String diasParaCorte) { this.diasParaCorte = diasParaCorte; }

    public String getCostoReactivacion() { return costoReactivacion; }
    public void setCostoReactivacion(String costoReactivacion) { this.costoReactivacion = costoReactivacion; }
}
