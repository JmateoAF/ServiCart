package servicart.ui.viewmodels.cliente;

public class ContratoViewModel {
    private int id;
    private String empresa;
    private String tipoServicio;
    private String tarifaTexto;
    private String fechaInicioTexto;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getTarifaTexto() { return tarifaTexto; }
    public void setTarifaTexto(String tarifaTexto) { this.tarifaTexto = tarifaTexto; }

    public String getFechaInicioTexto() { return fechaInicioTexto; }
    public void setFechaInicioTexto(String fechaInicioTexto) { this.fechaInicioTexto = fechaInicioTexto; }
}