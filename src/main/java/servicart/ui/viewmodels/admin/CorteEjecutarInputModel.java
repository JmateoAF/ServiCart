package servicart.ui.viewmodels.admin;

public class CorteEjecutarInputModel {
    private String contratoId;
    private String motivo;           // No está en CorteServicio actualmente, pero se usa en la UI
    private String observaciones;    // Ídem

    public String getContratoId() { return contratoId; }
    public void setContratoId(String contratoId) { this.contratoId = contratoId; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
