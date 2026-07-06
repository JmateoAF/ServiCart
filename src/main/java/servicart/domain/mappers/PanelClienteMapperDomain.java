package servicart.domain.mappers;

import servicart.domain.dtos.salidas.FacturaPendienteDTOSalida;
import servicart.domain.dtos.salidas.ServicioContratadoDTOSalida;
import servicart.entities.Contrato;
import servicart.entities.Factura;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PanelClienteMapperDomain {

    public static ServicioContratadoDTOSalida entidadADTO(Contrato contrato, List<Factura> pendientes) {
        double deuda = pendientes.stream().mapToDouble(Factura::getValorTotal).sum();
        List<FacturaPendienteDTOSalida> facturasDTO = pendientes.stream().map(PanelClienteMapperDomain::facturaADTO).toList();

        return new ServicioContratadoDTOSalida(
                contrato.getId(),
                contrato.getServicio().getTipo().name(),
                contrato.getServicio().getEmpresa().getNombre(),
                contrato.estaActivo() ? "Activo" : "Terminado",
                deuda,
                facturasDTO
        );
    }

    private static FacturaPendienteDTOSalida facturaADTO(Factura factura) {
        long dias = factura.estaVencida() ? ChronoUnit.DAYS.between(factura.getFechaVencimiento(), LocalDateTime.now()) : 0;
        return new FacturaPendienteDTOSalida(factura.getId(), factura.getValorTotal(), factura.getFechaVencimiento(), dias);
    }
}