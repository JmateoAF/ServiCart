package servicart.ui.viewmodels.cliente;

public class PerfilClienteViewModel {
    private String cedula;
    private String nombre;
    private String email;
    private String celular;
    private int activo; //Activo = 1, Inactivo = 0

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public int getActivo() { return activo; }
    public void setActivo(int activo) { this.activo = activo; }
}