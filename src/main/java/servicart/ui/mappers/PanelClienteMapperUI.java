package servicart.ui.mappers;

import servicart.domain.dtos.retornos.FacturaPendienteDTORetorno;
import servicart.domain.dtos.retornos.ServicioContratadoDTORetorno;
import servicart.ui.viewmodels.cliente.FacturaPendienteViewModel;
import servicart.ui.viewmodels.cliente.ServicioContratadoViewModel;
import java.time.format.DateTimeFormatter;

public class PanelClienteMapperUI {
    private static final DateTimeFormatter FORMATO_CORTO = DateTimeFormatter.ofPattern("dd MMMM");
    private static final DateTimeFormatter FORMATO_PERIODO = DateTimeFormatter.ofPattern("MMMM yy");

    public static ServicioContratadoViewModel DTOAviewModel(ServicioContratadoDTORetorno dto) {
        ServicioContratadoViewModel vm = new ServicioContratadoViewModel();
        vm.setIdContrato(dto.idContrato());
        vm.setNombreServicio(TipoServicios.nombreServicio(dto.nombreServicio()));
        vm.setEmpresa(dto.empresa());
        vm.setEstaCortado(dto.estaCortado());
        boolean hayMora = dto.facturasPendientes().stream().anyMatch(f -> f.diasMora() > 0);
        vm.setConMora(hayMora && !dto.estaCortado());
        vm.setCostoReactivacion(dto.costoReactivacion());
        vm.setCostoReactivacionTexto(String.format("$ %.2f", dto.costoReactivacion()));
        vm.setSubtotalServicioTexto(String.format("$ %.2f", dto.deudaTotal()));
        vm.setListaFacturas(dto.facturasPendientes().stream().map(PanelClienteMapperUI::facturaAViewModel).toList());
        return vm;
    }

    private static FacturaPendienteViewModel facturaAViewModel(FacturaPendienteDTORetorno dto) {
        FacturaPendienteViewModel vm = new FacturaPendienteViewModel();
        vm.setIdFactura(dto.idFactura());
        vm.setValorBase(dto.valorBase());
        vm.setValorTotal(dto.valorTotal());
        vm.setPeriodoTexto(capitalizar(dto.fechaEmision().format(FORMATO_PERIODO)));
        vm.setFechaVencimientoTexto(dto.fechaVencimiento().format(FORMATO_CORTO));
        vm.setFechaCorteTexto(dto.fechaCorte().format(FORMATO_CORTO));
        vm.setInteresAcumulado(dto.interesAcumulado());
        vm.setTieneMora(dto.diasMora() > 0);
        vm.setTotalIndividualTexto(String.format("$ %.2f", dto.valorTotal()));
        return vm;
    }

    private static String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }
}