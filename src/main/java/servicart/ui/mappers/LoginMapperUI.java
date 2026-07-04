package servicart.ui.mappers;

import servicart.domain.dtos.LoginDTOEntrada;
import servicart.domain.dtos.LoginDTOSalida;
import servicart.ui.viewmodels.LoginViewModel;

public class LoginMapperUI {
    public static LoginDTOEntrada viewModelADTO(LoginViewModel viewModel) {
        return new LoginDTOEntrada(viewModel.getCedula(), viewModel.getBaseDatos());
    }

    public static LoginViewModel dtoAViewModel(LoginDTOSalida dto) {
        LoginViewModel viewModel = new LoginViewModel();
        viewModel.setCedula(dto.cedula());
        viewModel.setBaseDatos(dto.baseDatos());
        return viewModel;
    }
}