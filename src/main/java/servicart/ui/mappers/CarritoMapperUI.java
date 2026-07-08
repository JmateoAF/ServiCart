package servicart.ui.mappers;

import servicart.domain.dtos.retornos.AbonoCarritoDTORetorno;
import servicart.domain.dtos.retornos.CarritoDTORetorno;
import servicart.ui.viewmodels.cliente.AbonoCarritoViewModel;
import servicart.ui.viewmodels.cliente.CarritoResumenViewModel;

public class CarritoMapperUI {

    public static CarritoResumenViewModel DTOAviewModel(CarritoDTORetorno dto) {
        CarritoResumenViewModel vm = new CarritoResumenViewModel();
        vm.setCantidadItems(dto.items().size());
        vm.setSubtotal(String.format("$ %.2f", dto.total()));
        vm.setTotal(String.format("$ %.2f", dto.total()));
        vm.setItems(dto.items().stream().map(CarritoMapperUI::itemAViewModel).toList());
        return vm;
    }

    private static AbonoCarritoViewModel itemAViewModel(AbonoCarritoDTORetorno dto) {
        AbonoCarritoViewModel vm = new AbonoCarritoViewModel();
        vm.setIdAbono(dto.idAbono());
        vm.setServicio(dto.descripcionServicio());
        vm.setMonto(String.format("$ %.2f", dto.monto()));
        vm.setReferenciaFactura("Factura #" + dto.idFactura());
        return vm;
    }
}