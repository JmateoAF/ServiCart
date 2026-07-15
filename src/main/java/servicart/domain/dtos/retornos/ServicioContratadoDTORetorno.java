package servicart.domain.dtos.retornos;

import java.util.List;

public record ServicioContratadoDTORetorno(int idContrato, String nombreServicio, String empresa, String estadoContrato,
                                           double deudaTotal, boolean estaCortado, double costoReactivacion,
                                           List<FacturaPendienteDTORetorno> facturasPendientes) {
}