package servicart.domain.mappers;

import servicart.ui.viewmodels.admin.CorteEjecutarInputModel;
import servicart.ui.dtos.CorteRequestDTO;

public class CorteMapper {
    public static CorteRequestDTO toRequest(CorteEjecutarInputModel input) {
        String contratoId = input.getContratoId().trim();
        return new CorteRequestDTO(
                contratoId,
                input.getMotivo() != null ? input.getMotivo().trim() : "",
                input.getObservaciones() != null ? input.getObservaciones().trim() : ""
        );
    }
}