package servicart.dtos;

public record FacturaDTO(int id, String empresa, String tipoServicio, String periodo, String fechaVencimiento,
                         String fechaCorte, double montoOriginal, double montoMora, double total, String estado,
                         boolean tieneMora, boolean cortePendiente, double costoReactivacion) {
}
