package servicart.domain.dtos.retornos;

import java.time.LocalDateTime;

public record ContratoDTORetorno(int id, String empresa, String tipoServicio, String tipoValor, double tarifaFija,
                                 double tarifaPorUnidad, LocalDateTime fechaInicio) {
}