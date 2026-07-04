package servicart.ui.viewmodels;

public class LoginViewModel {
    private String cedula;
    private String baseDatos;  //1) SQLIte, 2) BInario

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getBaseDatos() { return baseDatos; }
    public void setBaseDatos(String baseDatos) { this.baseDatos = baseDatos; }
}