package servicart.domain.services.cliente;

import servicart.entities.Factura;

import java.util.List;

public class PanelClienteService {

    public boolean estaCortado(List<Factura> pendientes) {
        return pendientes.stream().anyMatch(Factura::superaFechaCorte);
    }

    public double calcularDeudaTotal(List<Factura> pendientes) {
        return pendientes.stream()
                .mapToDouble(Factura::getValorTotal)
                .sum();
    }
}
