package servicart.ui.mappers;

import servicart.domain.dtos.LoginClienteDTOEntrada;
import servicart.domain.dtos.LoginClienteDTOSalida;
import servicart.ui.viewmodels.LoginViewModel;

public class LoginMapperUI {
    public static LoginClienteDTOEntrada viewModelADTO(LoginViewModel viewModel) {
        return new LoginClienteDTOEntrada(viewModel.getCedula(), viewModel.getBaseDatos());
    }

    public static LoginViewModel dtoAViewModel(LoginClienteDTOSalida dto) {
        LoginViewModel viewModel = new LoginViewModel();
        viewModel.setCedula(dto.cedula());
        viewModel.setBaseDatos(dto.baseDatos());
        return viewModel;
    }
}