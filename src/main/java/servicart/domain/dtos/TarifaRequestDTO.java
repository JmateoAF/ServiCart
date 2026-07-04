package servicart.domain.dtos;

public class TarifaRequestDTO {
    private final double tarifaFija;
    private final double tarifaPorUnidad;
    private final double interesMoraDiario;
    private final int diasParaCorte;
    private final double costoReactivacion;

    public TarifaRequestDTO(double tarifaFija, double tarifaPorUnidad, double interesMoraDiario,
                            int diasParaCorte, double costoReactivacion) {
        this.tarifaFija = tarifaFija;
        this.tarifaPorUnidad = tarifaPorUnidad;
        this.interesMoraDiario = interesMoraDiario;
        this.diasParaCorte = diasParaCorte;
        this.costoReactivacion = costoReactivacion;
    }

    public double getTarifaFija() { return tarifaFija; }
    public double getTarifaPorUnidad() { return tarifaPorUnidad; }
    public double getInteresMoraDiario() { return interesMoraDiario; }
    public int getDiasParaCorte() { return diasParaCorte; }
    public double getCostoReactivacion() { return costoReactivacion; }
}
