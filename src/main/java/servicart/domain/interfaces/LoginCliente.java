package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.LoginClienteDTOEntrada;
import servicart.domain.dtos.salidas.LoginClienteDTOSalida;

public interface LoginCliente {
    LoginClienteDTOSalida validarLoginCliente(LoginClienteDTOEntrada dto);
}