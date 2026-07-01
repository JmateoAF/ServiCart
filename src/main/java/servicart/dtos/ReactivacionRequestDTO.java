package servicart.dtos;

public record ReactivacionRequestDTO(
        int corteId,
        double costoReactivacionPagado
) {}