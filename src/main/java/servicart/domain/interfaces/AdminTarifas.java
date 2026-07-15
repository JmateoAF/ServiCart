package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.ActualizarTarifaDTOEntrada;
import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;

import java.util.List;

public interface AdminTarifas {
    List<TarifaDetalleDTORetorno> listarTarifas();

    void actualizarTarifa(ActualizarTarifaDTOEntrada dto);
}
