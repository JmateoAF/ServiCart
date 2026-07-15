package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.ForzarCorteDTOEntrada;
import servicart.domain.dtos.entradas.PagarFacturaDTOEntrada;
import servicart.domain.dtos.entradas.ReactivarCorteDTOEntrada;
import servicart.domain.dtos.retornos.CorteDTORetorno;
import servicart.domain.dtos.retornos.FacturaEnMoraDTORetorno;
import servicart.domain.dtos.retornos.ResumenCortesDTORetorno;

import java.util.List;

public interface AdminCortes {
    ResumenCortesDTORetorno obtenerResumen();

    List<FacturaEnMoraDTORetorno> listarEnMoraSinCorte();

    List<CorteDTORetorno> listarCortados();

    void forzarCorte(ForzarCorteDTOEntrada dto);

    void reactivar(ReactivarCorteDTOEntrada dto);

    void pagarFactura(PagarFacturaDTOEntrada dto);
}
