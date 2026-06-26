package servicart.domain.interfaces;

import servicart.domain.models.entities.Factura;

public interface Observador {
    void actualizar(Factura factura);
}