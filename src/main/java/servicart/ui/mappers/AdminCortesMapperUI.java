package servicart.ui.mappers;

import servicart.domain.dtos.retornos.CorteDTORetorno;
import servicart.domain.dtos.retornos.FacturaEnMoraDTORetorno;
import servicart.domain.dtos.retornos.ResumenCortesDTORetorno;
import servicart.ui.viewmodels.admin.CorteDetalleViewModel;
import servicart.ui.viewmodels.admin.CorteResumenViewModel;

public class AdminCortesMapperUI {

    public static CorteResumenViewModel resumenAViewModel(ResumenCortesDTORetorno dto) {
        CorteResumenViewModel vm = new CorteResumenViewModel();
        vm.setTotalCortados(dto.totalCortados());
        vm.setTotalEnMora(dto.totalEnMora());
        vm.setTotalInteresesGenerados(String.format("%.2f", dto.interesesGenerados()));
        return vm;
    }

    public static CorteDetalleViewModel enMoraADetalle(FacturaEnMoraDTORetorno dto) {
        CorteDetalleViewModel vm = new CorteDetalleViewModel();
        vm.setIdContrato(dto.idContrato());
        vm.setIdCorte(-1);
        vm.setIdFactura(dto.idFactura());
        vm.setCortado(false);
        vm.setNombreCliente(dto.cliente());
        vm.setServicio(nombreServicioCompleto(dto.empresa(), dto.servicio()));
        vm.setDiasMora((int) dto.diasMora());
        vm.setDeudaOriginal(String.format("%.2f", dto.deudaOriginal()));
        vm.setInteresAcumulado(String.format("+ $%.2f", dto.interesAcumulado()));
        vm.setCostoReactivacion(null);
        vm.setTotal(String.format("%.2f", dto.deudaOriginal() + dto.interesAcumulado()));
        vm.setSaldoPendiente(dto.deudaOriginal() + dto.interesAcumulado());
        vm.setPieTexto(Math.min(dto.diasMora(), dto.ventanaDias()) + "/" + dto.ventanaDias() + " días para corte");
        return vm;
    }

    public static CorteDetalleViewModel corteADetalle(CorteDTORetorno dto) {
        CorteDetalleViewModel vm = new CorteDetalleViewModel();
        vm.setIdContrato(dto.idContrato());
        vm.setIdCorte(dto.idCorte());
        vm.setIdFactura(dto.idFactura());
        vm.setCortado(true);
        vm.setNombreCliente(dto.cliente());
        vm.setServicio(nombreServicioCompleto(dto.empresa(), dto.servicio()));
        vm.setDiasMora((int) dto.diasCortado());
        vm.setDeudaOriginal(String.format("%.2f", dto.deudaOriginal()));
        vm.setInteresAcumulado(String.format("+ $%.2f", dto.interesAcumulado()));
        vm.setCostoReactivacion(String.format("%.2f", dto.costoReactivacion()));
        vm.setTotal(String.format("%.2f", dto.deudaOriginal() + dto.interesAcumulado() + dto.costoReactivacion()));
        vm.setSaldoPendiente(dto.deudaOriginal() + dto.interesAcumulado());
        vm.setPieTexto("Servicio cortado");
        return vm;
    }

    private static String nombreServicioCompleto(String empresa, String tipoServicio) {
        return empresa + " (" + TipoServicios.nombreServicio(tipoServicio) + ")";
    }
}
