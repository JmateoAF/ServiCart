package servicart.ui.viewmodels.cliente;

public class LoginClienteViewModel {
    private String cedula;
    private String baseDatos;  //1) SQLIte, 2) BInario
    private int activo;

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getBaseDatos() { return baseDatos; }
    public void setBaseDatos(String baseDatos) { this.baseDatos = baseDatos; }

    public int getActivo() { return activo; }
    public void setActivo(int activo) { this.activo = activo; }
}