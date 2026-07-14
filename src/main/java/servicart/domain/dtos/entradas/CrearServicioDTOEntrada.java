package servicart.domain.dtos.entradas;

public record CrearServicioDTOEntrada(
        int idEmpresa,
        String tipoServicio,
        String tipoValor,
        double tarifaBase,
        double tasaInteresDiarioPorcentaje,
        double costoReactivacion,
        int diasParaCorte
) { }
