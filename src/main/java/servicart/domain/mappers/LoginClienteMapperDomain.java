package servicart.domain.mappers;

import servicart.domain.dtos.retornos.LoginClienteDTORetorno;
import servicart.entities.Cliente;

public class LoginClienteMapperDomain {
    public static LoginClienteDTORetorno entidadADTO(Cliente cliente, String baseDatos, int activo) {
        return new LoginClienteDTORetorno(cliente.getCedula(), cliente.getNombre(), cliente.getEmail(), cliente.getCelular(), activo, baseDatos);
    }
}