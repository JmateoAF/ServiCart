// Mapper
package servicart.domain.mappers;

import servicart.ui.viewmodels.admin.UsuarioInputModel;
import servicart.ui.dtos.ClienteRequestDTO;

public class UsuarioMapper {
    public static ClienteRequestDTO toRequest(UsuarioInputModel input) {
        int activo = "Activo".equalsIgnoreCase(input.getActivo()) ? 1 : 0;
        return new ClienteRequestDTO(
                input.getCedula().trim(),
                input.getNombre().trim(),
                input.getEmail().trim(),
                input.getCelular().trim(),
                activo
        );
    }
}