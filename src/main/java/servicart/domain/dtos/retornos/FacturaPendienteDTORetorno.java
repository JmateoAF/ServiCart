package servicart.domain.dtos.retornos;

import java.time.LocalDateTime;

public record FacturaPendienteDTORetorno(int idFactura, double valorBase, double saldoPendiente, LocalDateTime fechaEmision, LocalDateTime fechaVencimiento, LocalDateTime fechaCorte, long diasMora, double interesAcumulado) { }