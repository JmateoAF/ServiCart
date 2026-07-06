package servicart.domain.dtos.salidas;

import java.time.LocalDateTime;

public record FacturaPendienteDTOSalida(int idFactura, double valorTotal, LocalDateTime fechaVencimiento, long diasMora) { }