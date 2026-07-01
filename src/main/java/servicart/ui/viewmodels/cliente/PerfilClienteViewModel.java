package servicart.ui.viewmodels.cliente;

import java.util.ArrayList;
import java.util.List;

public class PerfilClienteViewModel {
    private String nombre;
    private String cedula;
    private String email;
    private String telefono;         // celular
    private String activo;           // "Activo" / "Inactivo"
    private List<ServicioRegistradoViewModel> servicios;

    public PerfilClienteViewModel() {
        servicios = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getActivo() { return activo; }
    public void setActivo(String activo) { this.activo = activo; }

    public List<ServicioRegistradoViewModel> getServicios() { return servicios; }
    public void setServicios(List<ServicioRegistradoViewModel> servicios) { this.servicios = servicios; }
}