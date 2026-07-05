package servicart.domain.dtos.salidas;

public record LoginClienteDTOSalida(String cedula, String nombre, String email, String celular, int activo, String baseDatos) { }