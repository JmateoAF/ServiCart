package servicart.ui.viewmodels.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TarifaTablaViewModel {
    private final StringProperty servicio;
    private final StringProperty empresa;
    private final StringProperty tarifa;    // ej. "$ 1.20 / m³" o "$ 29.99 fijo"
    private final StringProperty tipo;      // "Fijo" / "Variable"

    public TarifaTablaViewModel(String servicio, String empresa, String tarifa, String tipo) {
        this.servicio = new SimpleStringProperty(servicio);
        this.empresa = new SimpleStringProperty(empresa);
        this.tarifa = new SimpleStringProperty(tarifa);
        this.tipo = new SimpleStringProperty(tipo);
    }

    public StringProperty servicioProperty() { return servicio; }
    public String getServicio() { return servicio.get(); }

    public StringProperty empresaProperty() { return empresa; }
    public String getEmpresa() { return empresa.get(); }

    public StringProperty tarifaProperty() { return tarifa; }
    public String getTarifa() { return tarifa.get(); }

    public StringProperty tipoProperty() { return tipo; }
    public String getTipo() { return tipo.get(); }
}