package servicart.ui.mappers;

import servicart.domain.dtos.entradas.LoginAdminDTOEntrada;
import servicart.domain.dtos.retornos.LoginAdminDTORetorno;
import servicart.ui.viewmodels.admin.LoginAdminViewModel;

public class LoginAdminMapperUI {
    public static LoginAdminDTOEntrada viewModelADTO(LoginAdminViewModel viewModel) {
        return new LoginAdminDTOEntrada(viewModel.getUsuario(), viewModel.getContrasenia());
    }

    public static LoginAdminViewModel DTOAviewModel(LoginAdminDTORetorno dto) {
        LoginAdminViewModel viewModel = new LoginAdminViewModel();
        viewModel.setUsuario(dto.usuario());
        viewModel.setContrasenia(dto.contrasenia());

        return viewModel;
    }
}
