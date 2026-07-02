package servicart.domain.interfaces;

import servicart.entities.Factura;

public interface Observador {
    void actualizar(Factura factura);
}