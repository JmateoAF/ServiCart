package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.CarritoDTOEntrada;
import servicart.domain.dtos.retornos.CarritoDTORetorno;

public interface CarritoCliente {
    CarritoDTORetorno verCarrito(CarritoDTOEntrada dto);

    void vaciarCarrito(CarritoDTOEntrada dto);
}