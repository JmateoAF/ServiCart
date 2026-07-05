package servicart.domain.mappers;

import servicart.domain.dtos.LoginClienteDTOSalida;
import servicart.entities.Cliente;

public class LoginMapperDomain {
    public static LoginClienteDTOSalida entidadADTO(Cliente cliente, String baseDatos) {
        return new LoginClienteDTOSalida(cliente.getCedula(), cliente.getNombre(), cliente.getEmail(), cliente.getCelular(), cliente.getActivo(), baseDatos);
    }
}