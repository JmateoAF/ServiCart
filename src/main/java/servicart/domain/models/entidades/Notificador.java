package servicart.domain.models.entidades;

public class Notificador {
    //Inicio de la clase notificador, usando el patron de diseño observer
    private final String prefijoSistema;
    public Notificador() {
        this.prefijoSistema = "Notificación";
    }

    public void enviarNotificacion(Cliente cliente, String mensajeAviso) {
        String mensajeFinal = prefijoSistema + mensajeAviso;
        mostrarAlertaPantalla(cliente.getNombre(), mensajeFinal);
    }

    private void mostrarAlertaPantalla(String nombreCliente, String contenido) {
    }
}
