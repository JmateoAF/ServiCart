package servicart.dtos;

import servicart.domain.models.entities.Cliente;
import java.util.List;

public final class ClienteMapper {
    private ClienteMapper() {}

    public static ClienteDTO toDTO(Cliente c) { return new ClienteDTO(c.getCedula(), c.getNombre(), c.getEmail(), c.getCelular()); }

    public static List<ClienteDTO> toDTO(List<Cliente> lista) { return lista.stream().map(ClienteMapper::toDTO).toList(); }

    public static Cliente toEntity(ClienteDTO dto) { return new Cliente(dto.cedula(), dto.nombre(), dto.email(), dto.celular()); }
}
