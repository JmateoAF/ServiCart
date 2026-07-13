package servicart.domain.mappers;

import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;
import servicart.entities.ServicioCatalogo;
import servicart.entities.enums.TipoValorFactura;

public class AdminTarifasMapperDomain {

    public static TarifaDetalleDTORetorno entidadADTO(ServicioCatalogo servicio) {
        double tarifaBase = servicio.getTipoValor() == TipoValorFactura.FIJO
                ? servicio.getTarifaFija()
                : servicio.getTarifaPorUnidad();

        return new TarifaDetalleDTORetorno(
                servicio.getId(),
                servicio.getTipo().name(),
                servicio.getEmpresa().getNombre(),
                servicio.getTipoValor().name(),
                tarifaBase,
                servicio.getTasaInteresDiario() * 100,
                servicio.getCostoReactivacion());
    }
}
