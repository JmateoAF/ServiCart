// Mapper
package servicart.ui.mappers;

import servicart.ui.viewmodels.admin.ReactivacionInputModel;
import servicart.dtos.ReactivacionRequestDTO;

public class ReactivacionMapper {
    public static ReactivacionRequestDTO toRequest(ReactivacionInputModel input) {
        int corteId = Integer.parseInt(input.getCorteId().trim());
        double costo = Double.parseDouble(input.getCostoReactivacionPagado().trim());
        return new ReactivacionRequestDTO(corteId, costo);
    }
}