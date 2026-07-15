package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.CarritoDTOEntrada;
import servicart.domain.dtos.entradas.ConfirmarPagoDTOEntrada;
import servicart.domain.dtos.retornos.CarritoDTORetorno;

public interface Checkout {
    CarritoDTORetorno obtenerResumen(CarritoDTOEntrada dto);

    void confirmarPago(ConfirmarPagoDTOEntrada dto);
}