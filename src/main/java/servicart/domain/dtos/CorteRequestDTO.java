package servicart.domain.dtos;

public class CorteRequestDTO {
    private final String contratoId;
    private final String motivo;
    private final String observaciones;

    public CorteRequestDTO(String contratoId, String motivo, String observaciones) {
        this.contratoId = contratoId;
        this.motivo = motivo;
        this.observaciones = observaciones;
    }

    public String getContratoId() { return contratoId; }
    public String getMotivo() { return motivo; }
    public String getObservaciones() { return observaciones; }
}
