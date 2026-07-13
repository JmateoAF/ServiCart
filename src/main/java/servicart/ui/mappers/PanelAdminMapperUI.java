package servicart.ui.mappers;

import servicart.domain.dtos.retornos.ResumenAdminDTORetorno;
import servicart.domain.dtos.retornos.TarifaDTORetorno;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;
import servicart.ui.viewmodels.admin.DashboardAdminViewModel;
import servicart.ui.viewmodels.admin.TarifaTablaViewModel;
import servicart.ui.viewmodels.admin.UsuarioTablaViewModel;

import java.util.List;

public class PanelAdminMapperUI {

    public static DashboardAdminViewModel construirViewModel(ResumenAdminDTORetorno resumen, List<UsuarioDTORetorno> usuarios, List<TarifaDTORetorno> tarifas) {
        DashboardAdminViewModel vm = new DashboardAdminViewModel();
        vm.setUsuariosActivos(resumen.usuariosActivos());
        vm.setCortados(resumen.cortados());
        vm.setConMora(resumen.conMora());
        vm.setModoActivo(resumen.modoActivo());
        vm.getListaUsuarios().setAll(usuarios.stream().map(PanelAdminMapperUI::usuarioAViewModel).toList());
        vm.getListaTarifas().setAll(tarifas.stream().map(PanelAdminMapperUI::tarifaAViewModel).toList());
        return vm;
    }

    private static UsuarioTablaViewModel usuarioAViewModel(UsuarioDTORetorno dto) {
        return new UsuarioTablaViewModel(dto.cedula(), dto.nombre(), dto.email(), dto.celular(), dto.activo() ? "Activo" : "Inactivo");
    }

    private static TarifaTablaViewModel tarifaAViewModel(TarifaDTORetorno dto) {
        return new TarifaTablaViewModel(dto.servicio(), dto.empresa(), dto.tarifa(), dto.tipo());
    }
}