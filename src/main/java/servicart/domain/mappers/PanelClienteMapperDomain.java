package servicart.domain.mappers;

import servicart.domain.dtos.salidas.FacturaPendienteDTOSalida;
import servicart.domain.dtos.salidas.ServicioContratadoDTOSalida;
import servicart.entities.Contrato;
import servicart.entities.Factura;
import servicart.entities.InteresMora;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PanelClienteMapperDomain {

    public static ServicioContratadoDTOSalida entidadADTO(Contrato contrato, List<Factura> pendientes, List<InteresMora> intereses) {
        double deuda = pendientes.stream().mapToDouble(Factura::getValorTotal).sum();
        List<FacturaPendienteDTOSalida> facturasDTO = pendientes.stream()
                .map(f -> facturaADTO(f, intereses))
                .toList();

        return new ServicioContratadoDTOSalida(
                contrato.getId(),
                contrato.getServicio().getTipo().name(),
                contrato.getServicio().getEmpresa().getNombre(),
                contrato.estaActivo() ? "Activo" : "Terminado",
                deuda,
                facturasDTO
        );
    }

    private static FacturaPendienteDTOSalida facturaADTO(Factura factura, List<InteresMora> intereses) {
        long dias = factura.estaVencida() ? ChronoUnit.DAYS.between(factura.getFechaVencimiento(), LocalDateTime.now()) : 0;

        double interesAcumulado = intereses.stream()
                .filter(m -> m.getFactura().getId() == factura.getId())
                .mapToDouble(InteresMora::getInteresAcumulado)
                .sum();

        return new FacturaPendienteDTOSalida(
                factura.getId(),
                factura.getValorTotal(),
                factura.getFechaEmision(),
                factura.getFechaVencimiento(),
                factura.getFechaCorte(),
                dias,
                interesAcumulado
        );
    }
}