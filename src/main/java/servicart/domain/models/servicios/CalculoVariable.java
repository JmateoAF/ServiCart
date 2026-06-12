package servicart.domain.models.servicios;

import java.io.Serializable;

public class CalculoVariable implements EstrategiaCalculo, Serializable {

    @Override
    public double calcular(double consumo, ServicioCatalogo servicio) {
        // Tarifa por unidad multiplicada por el consumo (en centavos)
        return servicio.getTarifaPorUnidad() * consumo;
    }
}
