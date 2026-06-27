package servicart.domain.models.entities;

import servicart.domain.interfaces.Observador;
import java.util.ArrayList;
import java.util.List;

/* Patrón Observer — rol Sujeto.
FacturacionService la extiende para notificar al emitir una factura */

public abstract class SujetoNotificable {
    private final List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador obs) { if (obs != null && !observadores.contains(obs)) observadores.add(obs); }

    public void quitarObservador(Observador obs) {
        observadores.remove(obs);
    }

    // Avisa a todos los observadores suscritos
    protected void notificarObservadores(Factura factura) {
        // Copia defensiva: permite desuscribirse durante la notificación
        for (Observador obs : new ArrayList<>(observadores)) obs.actualizar(factura);
    }
}