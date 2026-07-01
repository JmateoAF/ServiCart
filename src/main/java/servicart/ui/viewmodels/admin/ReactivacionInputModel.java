package servicart.ui.viewmodels.admin;

public class ReactivacionInputModel {
    private String corteId;                 // coincide con CorteServicio.id
    private String costoReactivacionPagado; // coincide con CorteServicio.costoReactivacionPagado

    public ReactivacionInputModel() {}

    public String getCorteId() { return corteId; }
    public void setCorteId(String corteId) { this.corteId = corteId; }

    public String getCostoReactivacionPagado() { return costoReactivacionPagado; }
    public void setCostoReactivacionPagado(String costoReactivacionPagado) {
        this.costoReactivacionPagado = costoReactivacionPagado;
    }
}
