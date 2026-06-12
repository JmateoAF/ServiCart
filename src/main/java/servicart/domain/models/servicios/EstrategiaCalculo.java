package servicart.domain.models.servicios;

import java.io.Serializable;

/**
 * Define la estrategia de cálculo del monto de una factura.
 * Cada implementación encapsula una fórmula distinta (fijo o variable).
 */
public interface EstrategiaCalculo extends Serializable {
    /**
     * Calcula el valor en base al consumo y los datos del servicio.
     *
     * @param consumo Cantidad consumida (en unidades correspondientes).
     * @param servicio El servicio del catálogo que contiene las tarifas.
     * @return Monto calculado en la misma unidad que las tarifas (centavos).
     */
    double calcular(double consumo, ServicioCatalogo servicio);
}