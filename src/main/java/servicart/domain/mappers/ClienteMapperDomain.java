package servicart.domain.mappers;

import servicart.domain.dtos.PerfilClienteDTO;
import servicart.entities.Cliente;

public class ClienteMapperDomain {
    public static PerfilClienteDTO entidadAPerfilDTO(Cliente cliente) {
        return new PerfilClienteDTO(
                cliente.getCedula(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getCelular(),
                cliente.getActivo()
        );
    }
}