package servicart.domain.mappers;

import servicart.domain.dtos.salidas.ContratoDTOSalida;
import servicart.entities.Contrato;
import servicart.entities.ServicioCatalogo;

public class ContratoMapperDomain {
    public static ContratoDTOSalida entidadADTO(Contrato contrato) {
        ServicioCatalogo servicio = contrato.getServicio();
        return new ContratoDTOSalida(
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