package servicart.domain.dtos.retornos;

public record TarifaDetalleDTORetorno(
        int id,
        String nombreServicio,
        String empresa,
        String tipoValor,
        double tarifaBase,
        double tasaInteresDiarioPorcentaje,
        double costoReactivacion
) { }
