package servicart.dtos;

public record ServicioResponseDTO(int id, String empresa, String tipoServicio, String tipoValor, double tarifa, double costoReactivacion, double tasaInteresDiario) {}
