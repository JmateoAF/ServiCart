package servicart.ui.mappers;

import servicart.domain.dtos.retornos.EmpresaDTORetorno;
import servicart.ui.viewmodels.admin.EmpresaTablaViewModel;

public class AdminEmpresasMapperUI {

    public static EmpresaTablaViewModel dtoAViewModel(EmpresaDTORetorno dto) {
        EmpresaTablaViewModel vm = new EmpresaTablaViewModel();
        vm.setId(dto.id());
        vm.setNombre(dto.nombre());
        vm.setCantidadServiciosTexto(dto.cantidadServicios() + (dto.cantidadServicios() == 1 ? " servicio" : " servicios"));
        return vm;
    }
}
