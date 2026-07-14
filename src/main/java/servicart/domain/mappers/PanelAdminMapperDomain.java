package servicart.domain.mappers;

import servicart.domain.dtos.retornos.ResumenAdminDTORetorno;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;
import servicart.entities.Cliente;

public class PanelAdminMapperDomain {

    public static UsuarioDTORetorno usuarioADTO(Cliente cliente) {
        return new UsuarioDTORetorno(
                cliente.getCedula(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getCelular(),
                cliente.getActivo() == 1);
    }

    public static ResumenAdminDTORetorno resumenADTO(int usuariosActivos, int cortados, int conMora, String modoActivo) {
        return new ResumenAdminDTORetorno(usuariosActivos, cortados, conMora, modoActivo);
    }
}