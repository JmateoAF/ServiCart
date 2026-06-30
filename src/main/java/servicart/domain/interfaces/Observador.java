package servicart.domain.interfaces;

import servicart.models.entities.Factura;

public interface Observador {
    void actualizar(Factura factura);
}