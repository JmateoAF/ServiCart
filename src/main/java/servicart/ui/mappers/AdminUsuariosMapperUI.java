package servicart.ui.mappers;

import servicart.domain.dtos.retornos.UsuarioDTORetorno;
import servicart.ui.viewmodels.admin.UsuarioTablaViewModel;

public class AdminUsuariosMapperUI {
    public static UsuarioTablaViewModel dtoAFila(UsuarioDTORetorno dto) {
        return new UsuarioTablaViewModel(dto.cedula(), dto.nombre(), dto.email(), dto.celular(), dto.activo() ? "Activo" : "Inactivo");
    }
}
