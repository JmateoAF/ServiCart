package servicart.domain.dtos;

public class ReactivacionRequestDTO {
    private final String corteId;
    private final double costoReactivacionPagado;

    public ReactivacionRequestDTO(String corteId, double costoReactivacionPagado) {
        this.corteId = corteId;
        this.costoReactivacionPagado = costoReactivacionPagado;
    }

    public String getCorteId() { return corteId; }
    public double getCostoReactivacionPagado() { return costoReactivacionPagado; }
}
