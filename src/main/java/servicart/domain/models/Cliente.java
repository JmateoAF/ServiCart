package servicart.domain.models;

public class Cliente {
    private final String cedula;
    private String nombre;
    private String email;
    private String celular;
    private int activo; //Activo = 1, Inactivo = 0

    public Cliente(String cedula, String nombre, String email, String celular, int activo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
        this.activo = activo;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }
    public void setCelular(String celular) {
        this.celular = celular;
    }

    public int getActivo() {
        return activo;
    }
    public void setActivo(int activo) {
        this.activo = activo;
    }
}