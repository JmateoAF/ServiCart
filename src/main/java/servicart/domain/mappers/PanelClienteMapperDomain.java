package servicart.domain.mappers;

import servicart.domain.dtos.retornos.FacturaPendienteDTORetorno;
import servicart.domain.dtos.retornos.ServicioContratadoDTORetorno;
import servicart.entities.Contrato;
import servicart.entities.Factura;

import java.util.List;
import java.util.Map;

public class PanelClienteMapperDomain {

    public static ServicioContratadoDTORetorno entidadADTO(
            Contrato contrato,
            List<Factura> pendientes,
            boolean estaCortado,
            double deudaTotal,
            Map<Integer, Double> saldosPendientes) {

        List<FacturaPendienteDTORetorno> facturasDTO = pendientes.stream()
                .map(factura -> facturaADTO(factura, saldosPendientes.get(factura.getId())))
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

    private static FacturaPendienteDTORetorno facturaADTO(Factura factura, double saldoPendiente) {
        return new FacturaPendienteDTORetorno(
                factura.getId(),
                factura.getValorBase(),
                saldoPendiente,
                factura.getFechaEmision(),
                factura.getFechaVencimiento(),
                factura.getFechaCorte(),
                factura.diasDeRetraso(),
                factura.interesAcumulado()
        );
    }
}