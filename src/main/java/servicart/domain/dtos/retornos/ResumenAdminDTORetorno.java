package servicart.domain.dtos.retornos;

public record ResumenAdminDTORetorno(int usuariosActivos, int cortados, int conMora, String modoActivo) { }