package servicart.entities;

import servicart.domain.interfaces.Observador;

import java.util.ArrayList;
import java.util.List;

/* Patrón Observer — rol Sujeto.
FacturacionService la extiende para notificar al emitir una factura */

public abstract class SujetoNotificable {
    private final transient List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador obs) {
        if (obs != null && !observadores.contains(obs)) observadores.add(obs);
    }

    // Avisa a todos los observadores suscritos
    protected void notificarObservadores(Factura factura) {
        //Permite desuscribirse durante la notificación
        for (Observador obs : new ArrayList<>(observadores)) obs.actualizar(factura);
    }
}