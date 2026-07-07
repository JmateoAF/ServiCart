package servicart.ui.viewmodels.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DashboardAdminViewModel {
    private int totalUsuarios;
    private int totalContratos;
    private int totalFacturas;
    private int totalCortes;
    private String rutaArchivos;
    private final ObservableList<UsuarioTablaViewModel> listaUsuarios;
    private final ObservableList<TarifaTablaViewModel> listaTarifas;

    public DashboardAdminViewModel() {
        listaUsuarios = FXCollections.observableArrayList();
        listaTarifas = FXCollections.observableArrayList();
    }

    public int getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(int totalUsuarios) { this.totalUsuarios = totalUsuarios; }

    public int getTotalContratos() { return totalContratos; }
    public void setTotalContratos(int totalContratos) { this.totalContratos = totalContratos; }

    public int getTotalFacturas() { return totalFacturas; }
    public void setTotalFacturas(int totalFacturas) { this.totalFacturas = totalFacturas; }

    public int getTotalCortes() { return totalCortes; }
    public void setTotalCortes(int totalCortes) { this.totalCortes = totalCortes; }

    public String getRutaArchivos() { return rutaArchivos; }
    public void setRutaArchivos(String rutaArchivos) { this.rutaArchivos = rutaArchivos; }

    public ObservableList<UsuarioTablaViewModel> getListaUsuarios() { return listaUsuarios; }
    public ObservableList<TarifaTablaViewModel> getListaTarifas() { return listaTarifas; }
}