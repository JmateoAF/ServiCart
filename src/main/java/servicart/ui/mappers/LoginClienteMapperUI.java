package servicart.ui.mappers;

import servicart.domain.dtos.entradas.LoginClienteDTOEntrada;
import servicart.domain.dtos.salidas.LoginClienteDTOSalida;
import servicart.ui.viewmodels.cliente.LoginClienteViewModel;

public class LoginClienteMapperUI {
    public static LoginClienteDTOEntrada viewModelADTO(LoginClienteViewModel viewModel) {
        return new LoginClienteDTOEntrada(viewModel.getCedula(), viewModel.getBaseDatos(), viewModel.getActivo());
    }

    public static LoginClienteViewModel DTOAviewModel(LoginClienteDTOSalida dto) {
        LoginClienteViewModel viewModel = new LoginClienteViewModel();
        viewModel.setCedula(dto.cedula());
        viewModel.setBaseDatos(dto.baseDatos());
        viewModel.setActivo(dto.activo());

        return viewModel;
    }
}