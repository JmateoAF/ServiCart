package servicart.ui.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import servicart.domain.dtos.retornos.ResumenAdminDTORetorno;
import servicart.domain.dtos.retornos.TarifaDTORetorno;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;
import servicart.domain.interfaces.PanelAdmin;
import servicart.domain.services.BdService;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.PanelAdminMapperUI;
import servicart.ui.viewmodels.admin.DashboardAdminViewModel;
import servicart.ui.viewmodels.admin.TarifaTablaViewModel;
import servicart.ui.viewmodels.admin.UsuarioTablaViewModel;

import java.util.List;

public class AdminDashboardController {
    @FXML private Button btnSQLite;
    @FXML private Button btnBinario;
    @FXML private Label lblModoActivo;
    @FXML private Label lblUsuariosActivos;
    @FXML private Label lblCortados;
    @FXML private Label lblConMora;

    @FXML private TableView<UsuarioTablaViewModel> tblUsuarios;
    @FXML private TableColumn<UsuarioTablaViewModel, String> colCedula;
    @FXML private TableColumn<UsuarioTablaViewModel, String> colNombre;
    @FXML private TableColumn<UsuarioTablaViewModel, String> colEmail;
    @FXML private TableColumn<UsuarioTablaViewModel, String> colEstado;

    @FXML private TableView<TarifaTablaViewModel> tblTarifas;
    @FXML private TableColumn<TarifaTablaViewModel, String> colServicio;
    @FXML private TableColumn<TarifaTablaViewModel, String> colEmpresa;
    @FXML private TableColumn<TarifaTablaViewModel, String> colTarifa;
    @FXML private TableColumn<TarifaTablaViewModel, String> colTipo;

    private final PanelAdmin panelAdmin;

    public AdminDashboardController(PanelAdmin panelAdmin) { this.panelAdmin = panelAdmin; }

    @FXML
    public void initialize() {
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("activo"));

        colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        colTarifa.setCellValueFactory(new PropertyValueFactory<>("tarifa"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        cargarDatos();
    }

    private void cargarDatos() {
        ResumenAdminDTORetorno resumen = panelAdmin.obtenerResumen();
        List<UsuarioDTORetorno> usuarios = panelAdmin.listarUsuarios();
        List<TarifaDTORetorno> tarifas = panelAdmin.listarTarifas();

        DashboardAdminViewModel vm = PanelAdminMapperUI.construirViewModel(resumen, usuarios, tarifas);

        lblUsuariosActivos.setText(String.valueOf(vm.getUsuariosActivos()));
        lblCortados.setText(String.valueOf(vm.getCortados()));
        lblConMora.setText(String.valueOf(vm.getConMora()));
        lblModoActivo.setText("Modo: " + vm.getModoActivo());

        tblUsuarios.setItems(vm.getListaUsuarios());
        tblTarifas.setItems(vm.getListaTarifas());

        actualizarEstiloToggle(vm.getModoActivo());
    }

    @FXML
    private void onSeleccionarSQLite(ActionEvent event) {
        cambiarModo("SQLite");
        event.consume();
    }

    @FXML
    private void onSeleccionarBinario(ActionEvent event) {
        cambiarModo("Binario");
        event.consume();
    }

    /* Reconfigura FactoryDAO para TODA la sesión de la app (login, panel cliente, checkout),
    no solo esta pantalla — ver Tooltip de los botones de toggle en adminDashboard.fxml */
    private void cambiarModo(String modo) {
        BdService.configurarBaseDatos(modo);
        cargarDatos();
    }

    private void actualizarEstiloToggle(String modoActivo) {
        boolean sqlite = "SQLite".equalsIgnoreCase(modoActivo);
        btnSQLite.setStyle(estiloToggle(sqlite) + " -fx-background-radius: 5 0 0 5;");
        btnBinario.setStyle(estiloToggle(!sqlite) + " -fx-background-radius: 0 5 5 0;");
    }

    private String estiloToggle(boolean activo) {
        return activo
                ? "-fx-background-color: #1e1c0f; -fx-text-fill: #e8c96d; -fx-border-color: transparent; -fx-padding: 5 15 5 15; -fx-cursor: hand;"
                : "-fx-background-color: #161616; -fx-text-fill: #555555; -fx-border-color: transparent; -fx-padding: 5 15 5 15; -fx-cursor: hand;";
    }

    @FXML private void onDashboard(ActionEvent event) { Navegador.irA("views/admin/adminDashboard.fxml"); event.consume(); }
    @FXML private void onUsuarios(ActionEvent event) { Navegador.irA("views/admin/adminUsuarios.fxml"); event.consume(); }
    @FXML private void onTarifas(ActionEvent event) { Navegador.irA("views/admin/adminTarifas.fxml"); event.consume(); }
    @FXML private void onCortes(ActionEvent event) { Navegador.irA("views/admin/adminCortes.fxml"); event.consume(); }

    @FXML
    private void onSalir(ActionEvent event) {
        Navegador.irA("views/admin/loginAdmin.fxml");
        event.consume();
    }
}