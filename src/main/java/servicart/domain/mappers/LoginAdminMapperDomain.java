package servicart.domain.mappers;

import servicart.domain.dtos.retornos.LoginAdminDTORetorno;

public class LoginAdminMapperDomain {
    public static LoginAdminDTORetorno entidadADTO(String usuario, String contrasenia) {
        return new LoginAdminDTORetorno(usuario, contrasenia);
    }
}
