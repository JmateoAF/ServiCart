package servicart.domain.mappers;

import servicart.dtos.ClienteDTO;
import servicart.models.entities.Cliente;

public class ClienteMapper {
    public Cliente aEntidad(ClienteDTO dto) {
        if (dto == null) return null;
        return new Cliente(dto.cedula(), dto.nombre(), dto.email(), dto.celular());
    }

    public ClienteDTO aDTO(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteDTO(cliente.getCedula(), cliente.getNombre(), cliente.getEmail(), cliente.getCelular());
    }
}