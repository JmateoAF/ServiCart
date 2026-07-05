package servicart.domain.mappers;

import servicart.domain.dtos.salidas.LoginClienteDTOSalida;
import servicart.entities.Cliente;

public class LoginClienteMapperDomain {
    public static LoginClienteDTOSalida entidadADTO(Cliente cliente, String baseDatos, int activo) {
        return new LoginClienteDTOSalida(cliente.getCedula(), cliente.getNombre(), cliente.getEmail(), cliente.getCelular(), activo, baseDatos);
    }
}