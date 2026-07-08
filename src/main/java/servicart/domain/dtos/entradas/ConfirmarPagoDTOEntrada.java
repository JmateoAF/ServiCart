package servicart.domain.dtos.entradas;

import servicart.entities.enums.ModalidadPago;

public record ConfirmarPagoDTOEntrada(String cedula, ModalidadPago modalidadPago) { }