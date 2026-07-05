package servicart.domain.mappers;

import servicart.domain.dtos.salidas.PerfilClienteDTOSalida;
import servicart.entities.Cliente;

public class ClienteMapperDomain {
    public static PerfilClienteDTOSalida entidadAPerfilDTO(Cliente cliente) {
        return new PerfilClienteDTOSalida(cliente.getCedula(), cliente.getNombre(), cliente.getEmail(), cliente.getCelular(), cliente.getActivo());
    }
}