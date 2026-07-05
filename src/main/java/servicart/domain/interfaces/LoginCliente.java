package servicart.domain.interfaces;

import servicart.domain.dtos.LoginClienteDTOEntrada;
import servicart.domain.dtos.LoginClienteDTOSalida;

public interface LoginCliente {
    LoginClienteDTOSalida validarLoginCliente(LoginClienteDTOEntrada dto);
}