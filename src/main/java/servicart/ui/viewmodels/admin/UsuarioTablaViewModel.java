package servicart.ui.viewmodels.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsuarioTablaViewModel {
    private final StringProperty cedula; // identificador natural, usado para acciones
    private final StringProperty nombre;
    private final StringProperty email;
    private final StringProperty celular;
    private final StringProperty activo;   //Activo o inactivo

    public UsuarioTablaViewModel(String cedula, String nombre, String email, String celular, String activo) {
        this.cedula = new SimpleStringProperty(cedula);
        this.nombre = new SimpleStringProperty(nombre);
        this.email = new SimpleStringProperty(email);
        this.celular = new SimpleStringProperty(celular);
        this.activo = new SimpleStringProperty(activo);
    }

    public String getCedula() {
        return cedula.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getCelular() {
        return celular.get();
    }

    public String getActivo() {
        return activo.get();
    }
}