package servicart.domain.mappers;

import servicart.domain.dtos.retornos.ContratoDTORetorno;
import servicart.entities.Contrato;
import servicart.entities.ServicioCatalogo;

public class ContratoMapperDomain {
    public static ContratoDTORetorno entidadADTO(Contrato contrato) {
        ServicioCatalogo servicio = contrato.getServicio();
        return new ContratoDTORetorno(
                contrato.getId(),
                servicio.getEmpresa().getNombre(),
                servicio.getTipo().name(),
                servicio.getTipoValor().name(),
                servicio.getTarifaFija(),
                servicio.getTarifaPorUnidad(),
                contrato.getFechaInicio()
        );
    }
}