package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.LoginClienteDTOEntrada;
import servicart.domain.dtos.retornos.LoginClienteDTORetorno;

public interface LoginCliente {
    LoginClienteDTORetorno validarLoginCliente(LoginClienteDTOEntrada dto);
}