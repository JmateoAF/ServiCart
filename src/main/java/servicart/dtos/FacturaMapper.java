package servicart.dtos;

import servicart.domain.models.entities.Factura;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class FacturaMapper {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static FacturaDTO toDTO(Factura f, double mora, boolean cortePendiente) {
        String empresa = f.getContrato().getServicio().getEmpresa().name();
        String tipo = f.getContrato().getServicio().getTipo().name();
        String periodo = f.getFechaEmision().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        String vencimiento = f.getFechaVencimiento().format(FMT);
        String corte = f.getFechaCorte().format(FMT);
        double costoReact = f.getContrato().getServicio().getCostoReactivacion();

        return new FacturaDTO(f.getId(), empresa, tipo, periodo, vencimiento, corte, f.getValorTotal(), mora, f.getValorTotal() + mora, f.getEstado().name(), mora > 0, cortePendiente, costoReact);
    }

    public static FacturaDTO toDTO(Factura f) {
        return toDTO(f, 0.0, false);
    }

    public static List<FacturaDTO> toDTO(List<Factura> lista) { return lista.stream().map(FacturaMapper::toDTO).toList(); }
}
