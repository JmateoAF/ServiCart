package servicart.dtos;

public record CorteRequestDTO(
        int contratoId,
        String motivo,
        String observaciones
) {}