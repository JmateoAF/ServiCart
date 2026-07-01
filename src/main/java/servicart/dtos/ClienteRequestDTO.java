package servicart.dtos;

public record ClienteRequestDTO(
        String cedula,
        String nombre,
        String email,
        String celular,
        int activo
) {}