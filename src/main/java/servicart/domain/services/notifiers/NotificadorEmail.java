package servicart.domain.services.notifiers;

import servicart.domain.interfaces.Observador;
import servicart.entities.Factura;

import java.util.ArrayList;
import java.util.List;

public class NotificadorEmail implements Observador {
    private final List<String> registro = new ArrayList<>();

    @Override
    public void actualizar(Factura factura) {
        String email = factura.getContrato().getCliente().getEmail();
        registro.add("[EMAIL] → " + email + " | Factura #" + factura.getId() + " | $" + factura.getValorTotal());
    }

    //La UI puede leer esto para mostrar confirmación al admin
    public List<String> getRegistro() { return List.copyOf(registro); }
}