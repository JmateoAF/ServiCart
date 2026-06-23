package servicart.models.servicios;

import java.io.Serializable;

/*
 * Define la estrategia de cálculo del monto de una factura.
 * Cada implementación encapsula una fórmula distinta (fijo o variable).
 */

public interface CalculoStrategy extends Serializable {
    double calcular(double consumo, ServicioCatalogo servicio);
}