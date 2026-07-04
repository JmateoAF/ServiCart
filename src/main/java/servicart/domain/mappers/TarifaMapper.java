// Mapper
package servicart.domain.mappers;

import servicart.ui.viewmodels.admin.ServicioCatalogoInputModel;
import servicart.domain.dtos.TarifaRequestDTO;

public class TarifaMapper {
    public static TarifaRequestDTO toRequest(ServicioCatalogoInputModel input) {
        double tarifaFija = parseDouble(input.getTarifaFija());
        double tarifaPorUnidad = parseDouble(input.getTarifaPorUnidad());
        double interes = parseDouble(input.getInteresMoraDiario());
        int dias = parseInt(input.getDiasParaCorte());
        double costo = parseDouble(input.getCostoReactivacion());

        return new TarifaRequestDTO(tarifaFija, tarifaPorUnidad, interes, dias, costo);
    }

    private static double parseDouble(String value) {
        if (value == null || value.isBlank()) return 0.0;
        return Double.parseDouble(value.trim());
    }

    private static int parseInt(String value) {
        if (value == null || value.isBlank()) return 0;
        return Integer.parseInt(value.trim());
    }
}