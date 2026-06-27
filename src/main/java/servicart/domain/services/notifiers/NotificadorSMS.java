package servicart.domain.services.notifiers;

import servicart.domain.interfaces.Observador;
import servicart.domain.models.entities.Factura;

import java.util.ArrayList;
import java.util.List;

public class NotificadorSMS implements Observador {
    private final List<String> registro = new ArrayList<>();

    @Override
    public void actualizar(Factura factura) {
        String celular = factura.getContrato().getCliente().getCelular();
        registro.add("[SMS] → " + celular + " | Factura #" + factura.getId() + " | $" + factura.getValorTotal());
    }

    public List<String> getRegistro() { return List.copyOf(registro); }
}