package servicart.ui.viewmodels.admin;

public class UsuarioInputModel {
    private String cedula;
    private String nombre;
    private String email;
    private String celular;   // antes telefono, ahora coincide con Cliente.celular
    private String activo;    // antes estado, ahora coincide con Cliente.activo (int 1/0, pero aquí String para la UI)

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getActivo() { return activo; }
    public void setActivo(String activo) { this.activo = activo; }
}
