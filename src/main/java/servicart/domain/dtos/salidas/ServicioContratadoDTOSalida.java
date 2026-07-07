package servicart.domain.dtos.salidas;

import java.util.List;

public record ServicioContratadoDTOSalida(
        int idContrato,
        String nombreServicio,
        String empresa,
        String estadoContrato,
        double deudaTotal,
        boolean estaCortado,
        double costoReactivacion,
        List<FacturaPendienteDTOSalida> facturasPendientes
) { }