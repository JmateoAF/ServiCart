package servicart.domain.interfaces;

import servicart.domain.dtos.LoginDTOEntrada;
import servicart.domain.dtos.LoginDTOSalida;

public interface LoginCliente {
    LoginDTOSalida validarLoginCliente(LoginDTOEntrada dto);
}