package servicart.domain.mappers;

import servicart.domain.dtos.retornos.FacturaPendienteDTORetorno;
import servicart.domain.dtos.retornos.ServicioContratadoDTORetorno;
import servicart.entities.Contrato;
import servicart.entities.Factura;

import java.util.List;
public class PanelClienteMapperDomain {

    public static ServicioContratadoDTORetorno entidadADTO(
            Contrato contrato,
            List<Factura> pendientes,
            boolean estaCortado,
            double deudaTotal) {

        List<FacturaPendienteDTORetorno> facturasDTO = pendientes.stream()
                .map(PanelClienteMapperDomain::facturaADTO)
                .toList();

        return new ServicioContratadoDTORetorno(
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

    private static FacturaPendienteDTORetorno facturaADTO(Factura factura) {
        return new FacturaPendienteDTORetorno(
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