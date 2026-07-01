package servicart.domain.mappers;

import servicart.dtos.ClienteResponseDTO;
import servicart.models.entities.Cliente;

public class ClienteMapper {
    public Cliente aEntidad(ClienteResponseDTO dto) {
        if (dto == null) return null;
        return new Cliente(dto.cedula(), dto.nombre(), dto.email(), dto.celular());
    }

    public ClienteResponseDTO aDTO(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteResponseDTO(cliente.getCedula(), cliente.getNombre(), cliente.getEmail(), cliente.getCelular());
    }
}