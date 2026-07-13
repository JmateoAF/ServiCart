package servicart.ui.mappers;

import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;
import servicart.ui.viewmodels.admin.ServicioCatalogoViewModel;

public class AdminTarifasMapperUI {
    // FacturacionService fija vencimiento = emisión+30 días y corte = emisión+45 días:
    // la ventana entre vencimiento y corte es siempre de 15 días, no es configurable por servicio
    private static final int DIAS_VENTANA_CORTE = 15;

    public static ServicioCatalogoViewModel dtoAViewModel(TarifaDetalleDTORetorno dto) {
        ServicioCatalogoViewModel vm = new ServicioCatalogoViewModel();
        vm.setId(dto.id());
        vm.setNombreServicio(icono(dto.nombreServicio()) + " " + TipoServicios.nombreServicio(dto.nombreServicio()));
        vm.setEmpresaNombre(dto.empresa());
        vm.setTipoValor(capitalizar(dto.tipoValor()));

        boolean fijo = "FIJO".equals(dto.tipoValor());
        vm.setTarifa(fijo
                ? String.format("%.2f / mes", dto.tarifaBase())
                : String.format("%.2f / %s", dto.tarifaBase(), unidad(dto.nombreServicio())));

        vm.setInteresMora(String.format("%.2f%%", dto.tasaInteresDiarioPorcentaje()));
        vm.setDiasParaCorte(DIAS_VENTANA_CORTE + " días tras el vencimiento");
        vm.setCostoReactivacion(String.format("%.2f", dto.costoReactivacion()));

        vm.setTarifaBaseValor(dto.tarifaBase());
        vm.setInteresMoraPorcentajeValor(dto.tasaInteresDiarioPorcentaje());
        vm.setCostoReactivacionValor(dto.costoReactivacion());

        return vm;
    }

    private static String icono(String tipoServicio) {
        return switch (tipoServicio) {
            case "AGUA" -> "💧";
            case "LUZ" -> "⚡";
            case "BASURA" -> "🗑";
            case "INTERNET" -> "📶";
            default -> "•";
        };
    }

    private static String unidad(String tipoServicio) {
        return switch (tipoServicio) {
            case "AGUA" -> "m³";
            case "LUZ" -> "kWh";
            default -> "unidad";
        };
    }

    private static String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.charAt(0) + texto.substring(1).toLowerCase();
    }
}
