package servicart.domain.dtos;

public record FacturaResponseDTO(int id, String empresa, String tipoServicio, String periodo, String fechaVencimiento,
                                 String fechaCorte, double montoOriginal, double montoMora, double total, String estado,
                                 boolean tieneMora, boolean cortePendiente, double costoReactivacion) {
}
