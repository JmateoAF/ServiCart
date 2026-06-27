package servicart.dtos;

public record ServicioDTO(int id, String empresa, String tipoServicio, String tipoValor, double tarifa, double costoReactivacion, double tasaInteresDiario) {}
