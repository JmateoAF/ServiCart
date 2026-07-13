package servicart.domain.dtos.retornos;

public record CorteDTORetorno(
        int idCorte,
        int idContrato,
        String cliente,
        String empresa,
        String servicio,
        long diasCortado,
        double deudaOriginal,
        double interesAcumulado,
        double costoReactivacion
) { }
