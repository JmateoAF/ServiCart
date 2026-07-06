package servicart.ui.mappers;

import servicart.domain.dtos.salidas.FacturaPendienteDTOSalida;
import servicart.domain.dtos.salidas.ServicioContratadoDTOSalida;
import servicart.ui.viewmodels.cliente.FacturaPendienteViewModel;
import servicart.ui.viewmodels.cliente.ServicioContratadoViewModel;

import java.time.format.DateTimeFormatter;

public class PanelClienteMapperUI {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static ServicioContratadoViewModel DTOAviewModel(ServicioContratadoDTOSalida dto) {
        ServicioContratadoViewModel vm = new ServicioContratadoViewModel();
        vm.setNombreServicio(dto.nombreServicio());
        vm.setEmpresa(dto.empresa());
        vm.setEstadoContrato(dto.estadoContrato());
        vm.setDeudaTotal(String.format("$ %.2f", dto.deudaTotal()));
        vm.setListaFacturas(dto.facturasPendientes().stream().map(PanelClienteMapperUI::facturaAViewModel).toList());
        return vm;
    }

    private static FacturaPendienteViewModel facturaAViewModel(FacturaPendienteDTOSalida dto) {
        FacturaPendienteViewModel vm = new FacturaPendienteViewModel();
        vm.setIdFactura(dto.idFactura());
        vm.setMonto(String.format("$ %.2f", dto.valorTotal()));
        vm.setFechaVencimiento(dto.fechaVencimiento().format(FORMATO));
        vm.setDiasMora(dto.diasMora() > 0 ? dto.diasMora() + " días" : "-");
        return vm;
    }
}