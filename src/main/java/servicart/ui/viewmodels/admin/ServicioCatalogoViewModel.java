package servicart.ui.viewmodels.admin;

public class ServicioCatalogoViewModel {
    private int id;
    private String nombreServicio;
    private String empresaNombre;
    private String tipoValor;         //Fijo o variable
    private String tarifa;            //Formateada según tipo, para mostrar en la tarjeta
    private String interesMora;       //Formateado, para mostrar en la tarjeta
    private String diasParaCorte;     //Formateado, para mostrar en la tarjeta
    private String costoReactivacion; //Formateado, para mostrar en la tarjeta

    // Valores crudos (sin formatear), para precargar el formulario de edición
    private double tarifaBaseValor;
    private double interesMoraPorcentajeValor;
    private double costoReactivacionValor;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public String getEmpresaNombre() { return empresaNombre; }
    public void setEmpresaNombre(String empresaNombre) { this.empresaNombre = empresaNombre; }

    public String getTipoValor() { return tipoValor; }
    public void setTipoValor(String tipoValor) { this.tipoValor = tipoValor; }

    public String getTarifa() { return tarifa; }
    public void setTarifa(String tarifa) { this.tarifa = tarifa; }

    public String getInteresMora() { return interesMora; }
    public void setInteresMora(String interesMora) { this.interesMora = interesMora; }

    public String getDiasParaCorte() { return diasParaCorte; }
    public void setDiasParaCorte(String diasParaCorte) { this.diasParaCorte = diasParaCorte; }

    public String getCostoReactivacion() { return costoReactivacion; }
    public void setCostoReactivacion(String costoReactivacion) { this.costoReactivacion = costoReactivacion; }

    public double getTarifaBaseValor() { return tarifaBaseValor; }
    public void setTarifaBaseValor(double tarifaBaseValor) { this.tarifaBaseValor = tarifaBaseValor; }

    public double getInteresMoraPorcentajeValor() { return interesMoraPorcentajeValor; }
    public void setInteresMoraPorcentajeValor(double interesMoraPorcentajeValor) { this.interesMoraPorcentajeValor = interesMoraPorcentajeValor; }

    public double getCostoReactivacionValor() { return costoReactivacionValor; }
    public void setCostoReactivacionValor(double costoReactivacionValor) { this.costoReactivacionValor = costoReactivacionValor; }
}
