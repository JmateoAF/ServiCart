package servicart.ui.viewmodels.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CorteHistorialViewModel {
    private final StringProperty usuario;
    private final StringProperty servicio;
    private final StringProperty diasMora;
    private final StringProperty interes;
    private final StringProperty estado;

    public CorteHistorialViewModel(String usuario, String servicio, String diasMora, String interes, String estado) {
        this.usuario = new SimpleStringProperty(usuario);
        this.servicio = new SimpleStringProperty(servicio);
        this.diasMora = new SimpleStringProperty(diasMora);
        this.interes = new SimpleStringProperty(interes);
        this.estado = new SimpleStringProperty(estado);
    }

    public StringProperty usuarioProperty() { return usuario; }
    public String getUsuario() { return usuario.get(); }

    public StringProperty servicioProperty() { return servicio; }
    public String getServicio() { return servicio.get(); }

    public StringProperty diasMoraProperty() { return diasMora; }
    public String getDiasMora() { return diasMora.get(); }

    public StringProperty interesProperty() { return interes; }
    public String getInteres() { return interes.get(); }

    public StringProperty estadoProperty() { return estado; }
    public String getEstado() { return estado.get(); }
}