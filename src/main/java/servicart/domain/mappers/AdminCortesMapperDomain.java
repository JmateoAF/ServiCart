package servicart.domain.mappers;

import servicart.domain.dtos.retornos.CorteDTORetorno;
import servicart.domain.dtos.retornos.FacturaEnMoraDTORetorno;
import servicart.entities.CorteServicio;
import servicart.entities.Factura;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AdminCortesMapperDomain {

    public static FacturaEnMoraDTORetorno enMoraADTO(Factura factura) {
        long ventanaDias = ChronoUnit.DAYS.between(factura.getFechaVencimiento(), factura.getFechaCorte());
        long diasParaCorte = Math.max(0, ChronoUnit.DAYS.between(LocalDateTime.now(), factura.getFechaCorte()));

        return new FacturaEnMoraDTORetorno(
                factura.getContrato().getId(),
                factura.getId(),
                factura.getContrato().getCliente().getNombre(),
                factura.getContrato().getServicio().getEmpresa().getNombre(),
                factura.getContrato().getServicio().getTipo().name(),
                factura.diasDeRetraso(),
                factura.getValorBase(),
                factura.interesAcumulado(),
                diasParaCorte,
                ventanaDias);
    }

    public static CorteDTORetorno corteADTO(CorteServicio corte) {
        long diasCortado = ChronoUnit.DAYS.between(corte.getFechaCorte(), LocalDateTime.now());

        return new CorteDTORetorno(
                corte.getId(),
                corte.getContrato().getId(),
                corte.getContrato().getCliente().getNombre(),
                corte.getContrato().getServicio().getEmpresa().getNombre(),
                corte.getContrato().getServicio().getTipo().name(),
                diasCortado,
                corte.getFactura().getValorBase(),
                corte.getFactura().interesAcumulado(),
                corte.getContrato().getServicio().getCostoReactivacion());
    }
}
