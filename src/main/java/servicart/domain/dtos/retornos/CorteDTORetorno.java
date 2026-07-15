package servicart.domain.dtos.retornos;

public record CorteDTORetorno(int idCorte, int idContrato, int idFactura, String cliente, String empresa,
                              String servicio, long diasCortado, double deudaOriginal, double interesAcumulado,
                              double saldoPendiente, double costoReactivacion) {
}
