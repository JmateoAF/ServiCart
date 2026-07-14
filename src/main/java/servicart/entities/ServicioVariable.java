package servicart.entities;

import servicart.domain.interfaces.CalculoStrategy;
import java.io.Serializable;

public class ServicioVariable implements CalculoStrategy, Serializable {
    @Override
    public double calcular(double consumo, ServicioCatalogo servicio) {
        // Tarifa por unidad multiplicada por el consumo
        return servicio.getTarifaPorUnidad() * consumo;
    }
}
