package servicart.ui.viewmodels.cliente;

public class LoginClienteInputModel {
    private String cedula;       // Coincide con Cliente.cedula
    private String baseDatos;    // "SQLite" o "Binario", según el ComboBox

    public LoginClienteInputModel() {}

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getBaseDatos() { return baseDatos; }
    public void setBaseDatos(String baseDatos) { this.baseDatos = baseDatos; }
}
