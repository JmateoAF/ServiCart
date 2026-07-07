package servicart.ui.viewmodels.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsuarioTablaViewModel {
    private final int id; // no se muestra, pero se usa para acciones
    private final StringProperty cedula;
    private final StringProperty nombre;
    private final StringProperty email;
    private final StringProperty celular;
    private final StringProperty activo;   //Activo o inactivo

    public UsuarioTablaViewModel(int id, String cedula, String nombre, String email, String celular, String activo) {
        this.id = id;
        this.cedula = new SimpleStringProperty(cedula);
        this.nombre = new SimpleStringProperty(nombre);
        this.email = new SimpleStringProperty(email);
        this.celular = new SimpleStringProperty(celular);
        this.activo = new SimpleStringProperty(activo);
    }

    public int getId() { return id; }

    public StringProperty cedulaProperty() { return cedula; }
    public String getCedula() { return cedula.get(); }

    public StringProperty nombreProperty() { return nombre; }
    public String getNombre() { return nombre.get(); }

    public StringProperty emailProperty() { return email; }
    public String getEmail() { return email.get(); }

    public StringProperty celularProperty() { return celular; }
    public String getCelular() { return celular.get(); }

    public StringProperty activoProperty() { return activo; }
    public String getActivo() { return activo.get(); }
}