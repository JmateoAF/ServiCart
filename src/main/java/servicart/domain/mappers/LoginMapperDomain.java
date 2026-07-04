package servicart.domain.mappers;

import servicart.domain.dtos.LoginDTOSalida;
import servicart.entities.Cliente;

public class LoginMapperDomain {
    public static LoginDTOSalida entidadADTO(Cliente cliente, String baseDatos) {
        return new LoginDTOSalida(cliente.getCedula(), cliente.getNombre(), cliente.getEmail(), cliente.getCelular(), cliente.getActivo(), baseDatos);
    }
}