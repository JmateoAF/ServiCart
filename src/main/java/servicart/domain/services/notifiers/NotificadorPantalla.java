package servicart.domain.services.notifiers;

import servicart.domain.interfaces.Observador;
import servicart.domain.models.entities.Factura;

import java.util.function.Consumer;

/* Canal PANTALLA — puente al Controller sin acoplar el dominio a JavaFX
El Controller pasa un Consumer<String> al construirlo:
new NotificadorPantalla(msg -> lblAviso.setText(msg))
Así el dominio no importa nada de javafx */

public class NotificadorPantalla implements Observador {
    private final Consumer<String> salida;

    public NotificadorPantalla(Consumer<String> salida) {
        this.salida = (salida != null) ? salida : msg -> {};
    }

    @Override
    public void actualizar(Factura factura) {
        String nombre = factura.getContrato().getCliente().getNombre();
        salida.accept("Factura #" + factura.getId() + " emitida para " + nombre + " | Total: $" + factura.getValorTotal());
    }
}