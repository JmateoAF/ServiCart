package servicart.dtos;

public record TarifaRequestDTO(
        double tarifaFija,
        double tarifaPorUnidad,
        double interesMoraDiario,
        int diasParaCorte,
        double costoReactivacion
) {}