package servicart.domain.dtos;

public record AbonoResponseDTO(int id, String descripcion, double monto, String modalidadPago, String fechaPago) {}
