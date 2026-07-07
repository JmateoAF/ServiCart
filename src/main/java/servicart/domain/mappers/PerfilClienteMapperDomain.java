package servicart.domain.mappers;

import servicart.domain.dtos.retornos.PerfilClienteDTORetorno;
import servicart.entities.Cliente;

public class PerfilClienteMapperDomain {
    public static PerfilClienteDTORetorno entidadAPerfilDTO(Cliente cliente) {
        return new PerfilClienteDTORetorno(cliente.getCedula(), cliente.getNombre(), cliente.getEmail(), cliente.getCelular(), cliente.getActivo());
    }
}