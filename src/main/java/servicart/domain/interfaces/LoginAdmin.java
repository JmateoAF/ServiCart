package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.LoginAdminDTOEntrada;
import servicart.domain.dtos.retornos.LoginAdminDTORetorno;

public interface LoginAdmin {
    LoginAdminDTORetorno validarLoginAdmin(LoginAdminDTOEntrada dto);
}
