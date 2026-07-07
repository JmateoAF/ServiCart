package servicart.ui.mappers;

import servicart.domain.dtos.retornos.ContratoDTORetorno;
import servicart.ui.viewmodels.cliente.ContratoViewModel;
import java.time.format.DateTimeFormatter;

public class ContratoMapperUI {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static ContratoViewModel dtoAViewModel(ContratoDTORetorno dto) {
        ContratoViewModel vm = new ContratoViewModel();
        vm.setId(dto.id());
        vm.setEmpresa(dto.empresa());
        vm.setTipoServicio(NombresServicio.nombreServicio(dto.tipoServicio()));
        vm.setTarifaTexto(formatearTarifa(dto));
        vm.setFechaInicioTexto(dto.fechaInicio().format(FORMATO_FECHA));
        return vm;
    }

    private static String formatearTarifa(ContratoDTORetorno dto) {
        if ("FIJO".equals(dto.tipoValor())) {
            return String.format("$%.2f / mes", dto.tarifaFija());
        }
        return String.format("$%.2f / unidad consumida", dto.tarifaPorUnidad());
    }
}