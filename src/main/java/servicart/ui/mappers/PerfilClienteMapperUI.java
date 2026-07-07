package servicart.ui.mappers;

import servicart.domain.dtos.retornos.PerfilClienteDTORetorno;
import servicart.ui.viewmodels.cliente.PerfilClienteViewModel;

public class PerfilClienteMapperUI {
    public static PerfilClienteViewModel dtoAViewModel(PerfilClienteDTORetorno dto) {
        PerfilClienteViewModel viewModel = new PerfilClienteViewModel();
        viewModel.setCedula(dto.cedula());
        viewModel.setNombre(dto.nombre());
        viewModel.setEmail(dto.email());
        viewModel.setCelular(dto.celular());
        viewModel.setActivo(dto.activo());
        return viewModel;
    }
}
