package servicart.ui.mappers;

import servicart.domain.dtos.salidas.ContratoDTOSalida;
import servicart.ui.viewmodels.cliente.ContratoViewModel;

import java.time.format.DateTimeFormatter;

public class ContratoMapperUI {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static ContratoViewModel dtoAViewModel(ContratoDTOSalida dto) {
        ContratoViewModel vm = new ContratoViewModel();
        vm.setId(dto.id());
        vm.setEmpresa(dto.empresa());
        vm.setTipoServicio(nombreLegible(dto.tipoServicio()));
        vm.setTarifaTexto(formatearTarifa(dto));
        vm.setFechaInicioTexto(dto.fechaInicio().format(FORMATO_FECHA));
        return vm;
    }

    private static String nombreLegible(String tipoServicio) {
        return switch (tipoServicio) {
            case "AGUA" -> "Agua Potable";
            case "LUZ" -> "Electricidad";
            case "BASURA" -> "Recolección de Basura";
            case "INTERNET" -> "Internet";
            default -> tipoServicio;
        };
    }

    private static String formatearTarifa(ContratoDTOSalida dto) {
        if ("FIJO".equals(dto.tipoValor())) {
            return String.format("$%.2f / mes", dto.tarifaFija());
        }
        return String.format("$%.2f / unidad consumida", dto.tarifaPorUnidad());
    }
}