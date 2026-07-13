package servicart.domain.interfaces;

import servicart.domain.dtos.retornos.ResumenAdminDTORetorno;
import servicart.domain.dtos.retornos.TarifaDTORetorno;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;

import java.util.List;

public interface PanelAdmin {
    ResumenAdminDTORetorno obtenerResumen();
    List<UsuarioDTORetorno> listarUsuarios();
    List<TarifaDTORetorno> listarTarifas();
}