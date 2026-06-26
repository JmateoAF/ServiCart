package servicart.domain.models.catalog;

import java.io.Serializable;

public class CalculoFijo implements CalculoStrategy, Serializable {
    @Override
    public double calcular(double consumo, ServicioCatalogo servicio) {
        // Tarifa fija independiente del consumo
        return servicio.getTarifaFija();
    }
}
