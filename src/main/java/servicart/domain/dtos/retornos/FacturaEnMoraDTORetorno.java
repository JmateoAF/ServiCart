package servicart.domain.dtos.retornos;

public record FacturaEnMoraDTORetorno(
        int idContrato,
        int idFactura,
        String cliente,
        String empresa,
        String servicio,
        long diasMora,
        double deudaOriginal,
        double interesAcumulado,
        long diasParaCorte,
        long ventanaDias
) { }
