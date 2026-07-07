package servicart.domain.mappers;

import servicart.domain.dtos.salidas.FacturaPendienteDTOSalida;
import servicart.domain.dtos.salidas.ServicioContratadoDTOSalida;
import servicart.entities.Contrato;
import servicart.entities.Factura;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
public class PanelClienteMapperDomain {

    public static ServicioContratadoDTOSalida entidadADTO(
            Contrato contrato,
            List<Factura> pendientes,
            boolean estaCortado,
            double deudaTotal) {

        List<FacturaPendienteDTOSalida> facturasDTO = pendientes.stream()
                .map(PanelClienteMapperDomain::facturaADTO)
                .toList();

        return new ServicioContratadoDTOSalida(
                contrato.getId(),
                contrato.getServicio().getTipo().name(),
                contrato.getServicio().getEmpresa().getNombre(),
                contrato.estaActivo() ? "Activo" : "Terminado",
                deudaTotal,
                estaCortado,
                contrato.getServicio().getCostoReactivacion(),
                facturasDTO
        );
    }

    private static FacturaPendienteDTOSalida facturaADTO(Factura factura) {
        return new FacturaPendienteDTOSalida(
                factura.getId(),
                factura.getValorBase(),
                factura.getValorTotal(),
                factura.getFechaEmision(),
                factura.getFechaVencimiento(),
                factura.getFechaCorte(),
                factura.diasDeRetraso(),
                factura.interesAcumulado()
        );
    }
}