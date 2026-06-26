package servicart.domain.models.catalog;

import java.io.Serializable;

public class CalculoVariable implements CalculoStrategy, Serializable {
    @Override
    public double calcular(double consumo, ServicioCatalogo servicio) {
        // Tarifa por unidad multiplicada por el consumo
        return servicio.getTarifaPorUnidad() * consumo;
    }
}
