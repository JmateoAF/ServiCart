package servicart.domain.dtos.retornos;

public record LoginClienteDTORetorno(String cedula, String nombre, String email, String celular, int activo,
                                     String baseDatos) {
}