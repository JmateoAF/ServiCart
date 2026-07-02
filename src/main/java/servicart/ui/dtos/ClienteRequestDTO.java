package servicart.ui.dtos;

public class ClienteRequestDTO {
    private final String cedula;
    private final String nombre;
    private final String email;
    private final String celular;
    private final int activo;

    public ClienteRequestDTO(String cedula, String nombre, String email, String celular, int activo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
        this.activo = activo;
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getCelular() { return celular; }
    public int getActivo() { return activo; }
}
