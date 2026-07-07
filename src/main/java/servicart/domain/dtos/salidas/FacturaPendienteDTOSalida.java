package servicart.domain.dtos.salidas;

import java.time.LocalDateTime;

public record FacturaPendienteDTOSalida(int idFactura, double valorBase,double valorTotal, LocalDateTime fechaEmision, LocalDateTime fechaVencimiento, LocalDateTime fechaCorte, long diasMora, double interesAcumulado) { }