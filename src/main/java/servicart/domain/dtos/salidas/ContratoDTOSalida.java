package servicart.domain.dtos.salidas;

import java.time.LocalDateTime;

public record ContratoDTOSalida(
        int id,
        String empresa,
        String tipoServicio,
        String tipoValor,
        double tarifaFija,
        double tarifaPorUnidad,
        LocalDateTime fechaInicio
) { }