package servicart.domain.models.servicios;

import java.io.Serializable;

public class CalculoFijo implements EstrategiaCalculo, Serializable {
    @Override
    public double calcular(double consumo, ServicioCatalogo servicio) {
        // Tarifa fija independiente del consumo (en centavos)
        return servicio.getTarifaFija();
    }
}
