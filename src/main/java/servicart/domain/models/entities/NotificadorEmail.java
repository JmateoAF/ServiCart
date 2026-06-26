package servicart.domain.models.entities;

import servicart.domain.interfaces.Observador;
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