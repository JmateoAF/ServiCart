package servicart.ui.viewmodels.admin;

public class EmpresaTablaViewModel {
    private int id;
    private String nombre;
    private String cantidadServiciosTexto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidadServiciosTexto() {
        return cantidadServiciosTexto;
    }

    public void setCantidadServiciosTexto(String cantidadServiciosTexto) {
        this.cantidadServiciosTexto = cantidadServiciosTexto;
    }
}
