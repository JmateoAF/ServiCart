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
        boolean estaCortado = pendientes.stream().anyMatch(Factura::superaFechaCorte);

        double costoReactivacion = contrato.getServicio().getCostoReactivacion();

        double deudaTotal = pendientes.stream()
                .mapToDouble(f -> f.getValorTotal() + calcularInteresEnVivo(f))
                .sum();
        if (estaCortado) deudaTotal += costoReactivacion; // el costo se suma UNA vez por servicio, no por factura

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
                costoReactivacion,
                facturasDTO
        );
    }

    private static FacturaPendienteDTOSalida facturaADTO(Factura factura) {
        long dias = diasDeRetraso(factura);
        double interesAcumulado = calcularInteresEnVivo(factura);

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

    private static double calcularInteresEnVivo(Factura factura) {
        if (!factura.estaVencida()) return 0.0;
        long dias = diasDeRetraso(factura);
        double tasa = factura.getContrato().getServicio().getTasaInteresDiario();
        return factura.getValorTotal() * tasa * dias;
    }

    private static long diasDeRetraso(Factura factura) {
        if (!factura.estaVencida()) return 0;
        return ChronoUnit.DAYS.between(factura.getFechaVencimiento(), LocalDateTime.now());
    }
}