package servicart.domain.dtos.entradas;

import servicart.entities.enums.ModalidadPago;

public record PagarFacturaDTOEntrada(int idFactura, double monto, ModalidadPago modalidadPago) {
}
