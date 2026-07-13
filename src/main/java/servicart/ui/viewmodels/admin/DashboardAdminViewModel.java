package servicart.ui.viewmodels.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DashboardAdminViewModel {
    private int usuariosActivos;
    private int cortados;
    private int conMora;
    private String modoActivo;
    private final ObservableList<UsuarioTablaViewModel> listaUsuarios;
    private final ObservableList<TarifaTablaViewModel> listaTarifas;

    public DashboardAdminViewModel() {
        listaUsuarios = FXCollections.observableArrayList();
        listaTarifas = FXCollections.observableArrayList();
    }

    public int getUsuariosActivos() { return usuariosActivos; }
    public void setUsuariosActivos(int usuariosActivos) { this.usuariosActivos = usuariosActivos; }

    public int getCortados() { return cortados; }
    public void setCortados(int cortados) { this.cortados = cortados; }

    public int getConMora() { return conMora; }
    public void setConMora(int conMora) { this.conMora = conMora; }

    public String getModoActivo() { return modoActivo; }
    public void setModoActivo(String modoActivo) { this.modoActivo = modoActivo; }

    public ObservableList<UsuarioTablaViewModel> getListaUsuarios() { return listaUsuarios; }
    public ObservableList<TarifaTablaViewModel> getListaTarifas() { return listaTarifas; }
}