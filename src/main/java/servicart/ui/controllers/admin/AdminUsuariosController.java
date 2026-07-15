package servicart.ui.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import servicart.data.FactoryDAO;
import servicart.domain.dtos.entradas.CambiarEstadoUsuarioDTOEntrada;
import servicart.domain.dtos.entradas.ContratarServicioDTOEntrada;
import servicart.domain.dtos.entradas.UsuarioDTOEntrada;
import servicart.domain.dtos.retornos.ResumenAdminDTORetorno;
import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;
import servicart.domain.interfaces.AdminUsuarios;
import servicart.domain.interfaces.PanelAdmin;
import servicart.domain.services.BdService;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.AdminTarifasMapperUI;
import servicart.ui.mappers.AdminUsuariosMapperUI;
import servicart.ui.viewmodels.admin.ServicioCatalogoViewModel;
import servicart.ui.viewmodels.admin.UsuarioTablaViewModel;

import java.util.List;

public class AdminUsuariosController {
    @FXML private Button btnSQLite;
    @FXML private Button btnBinario;
    @FXML private Label lblUsuariosActivos;
    @FXML private Label lblCortados;
    @FXML private Label lblConMora;
    @FXML private TextField txtBuscar;
    @FXML private Label lblConteo;
    @FXML private VBox listaUsuarios;

    @FXML private TextField txtCedulaForm;
    @FXML private TextField txtNombreForm;
    @FXML private TextField txtEmailForm;
    @FXML private TextField txtCelularForm;
    @FXML private ComboBox<String> cmbEstadoForm;

    @FXML private Label lblClienteContratar;
    @FXML private ComboBox<ServicioCatalogoViewModel> cmbServicioContratar;
    @FXML private Button btnContratar;

    private final AdminUsuarios adminUsuarios;
    private final PanelAdmin panelAdmin;
    private boolean editando = false;
    private String cedulaParaContratar = null;

    public AdminUsuariosController(AdminUsuarios adminUsuarios, PanelAdmin panelAdmin) {
        this.adminUsuarios = adminUsuarios;
        this.panelAdmin = panelAdmin;
    }

    @FXML
    public void initialize() {
        cmbEstadoForm.getItems().setAll("Activo", "Inactivo");
        configurarComboServicios();

        txtBuscar.textProperty().addListener((obs, viejo, nuevo) -> cargarUsuarios());

        limpiarFormulario();
        limpiarSeleccionContratar();
        cargarResumen();
        cargarUsuarios();
        actualizarEstiloToggle(FactoryDAO.obtenerModoActual());
    }

    private void configurarComboServicios() {
        javafx.util.StringConverter<ServicioCatalogoViewModel> conversor = new javafx.util.StringConverter<>() {
            @Override
            public String toString(ServicioCatalogoViewModel s) {
                return s == null ? "" : s.getEmpresaNombre() + " (" + s.getNombreServicio() + ") - " + s.getTarifa();
            }
            @Override
            public ServicioCatalogoViewModel fromString(String s) { return null; }
        };
        cmbServicioContratar.setConverter(conversor);
    }

    private void cargarResumen() {
        ResumenAdminDTORetorno resumen = panelAdmin.obtenerResumen();
        lblUsuariosActivos.setText(String.valueOf(resumen.usuariosActivos()));
        lblCortados.setText(String.valueOf(resumen.cortados()));
        lblConMora.setText(String.valueOf(resumen.conMora()));
    }

    private void cargarUsuarios() {
        List<UsuarioDTORetorno> dtos = adminUsuarios.listarUsuarios(txtBuscar.getText());
        List<UsuarioTablaViewModel> filas = dtos.stream().map(AdminUsuariosMapperUI::dtoAFila).toList();

        listaUsuarios.getChildren().setAll(filas.stream().map(this::crearFilaUsuario).toList());
        lblConteo.setText(filas.size() + " registros");
    }

    private HBox crearFilaUsuario(UsuarioTablaViewModel u) {
        Label cedula = new Label(u.getCedula());
        cedula.setPrefWidth(110);
        cedula.setStyle("-fx-text-fill: #888888; -fx-font-family: 'Courier New'; -fx-font-size: 15;");

        Label nombre = new Label(u.getNombre());
        nombre.setPrefWidth(150);
        nombre.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 15;");

        Label email = new Label(u.getEmail());
        email.setPrefWidth(200);
        email.setStyle("-fx-text-fill: #888888; -fx-font-size: 15;");

        Label celular = new Label(u.getCelular());
        celular.setPrefWidth(110);
        celular.setStyle("-fx-text-fill: #888888; -fx-font-family: 'Courier New'; -fx-font-size: 15;");

        boolean activo = "Activo".equals(u.getActivo());
        Label estado = new Label(u.getActivo());
        estado.setPrefWidth(80);
        estado.setStyle((activo ? "-fx-text-fill: #27ae60; -fx-background-color: #0f2a18;" : "-fx-text-fill: #c0392b; -fx-background-color: #2a1010;")
                + " -fx-font-size: 15; -fx-padding: 0 10 0 10; -fx-background-radius: 10;");

        Button btnEditar = new Button("Editar");
        btnEditar.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #e8c96d; -fx-border-color: #2e2e2e; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand; -fx-font-size: 15; -fx-padding: 5 10 5 10;");
        btnEditar.setOnAction(event -> { cargarEnFormulario(u); event.consume(); });

        Button btnEstado = new Button(activo ? "Desactivar" : "Activar");
        btnEstado.setStyle((activo
                ? "-fx-background-color: #2a1010; -fx-text-fill: #c0392b; -fx-border-color: #3a1a1a;"
                : "-fx-background-color: #0f2a18; -fx-text-fill: #27ae60; -fx-border-color: #1a3e28;")
                + " -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand; -fx-font-size: 15; -fx-padding: 5 10 5 10;");
        btnEstado.setOnAction(event -> { onCambiarEstado(u); event.consume(); });

        Button btnContratarFila = new Button("Contratar");
        btnContratarFila.setStyle("-fx-background-color: #0f2a18; -fx-text-fill: #27ae60; -fx-border-color: #1a3e28; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand; -fx-font-size: 15; -fx-padding: 5 10 5 10;");
        btnContratarFila.setOnAction(event -> { seleccionarClienteParaContratar(u); event.consume(); });

        HBox acciones = new HBox(10, btnEditar, btnEstado, btnContratarFila);

        Region relleno = new Region();
        HBox.setHgrow(relleno, Priority.ALWAYS);

        HBox fila = new HBox(15, cedula, nombre, email, celular, estado, acciones, relleno);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setStyle("-fx-padding: 10 15 10 15; -fx-border-color: #1e1e1e; -fx-border-width: 0 0 1 0;");
        return fila;
    }

    private void cargarEnFormulario(UsuarioTablaViewModel fila) {
        editando = true;
        txtCedulaForm.setText(fila.getCedula());
        txtCedulaForm.setEditable(false);
        txtNombreForm.setText(fila.getNombre());
        txtEmailForm.setText(fila.getEmail());
        txtCelularForm.setText(fila.getCelular());
        cmbEstadoForm.setValue(fila.getActivo());
    }

    private void onCambiarEstado(UsuarioTablaViewModel fila) {
        boolean nuevoEstado = !"Activo".equals(fila.getActivo());
        adminUsuarios.cambiarEstado(new CambiarEstadoUsuarioDTOEntrada(fila.getCedula(), nuevoEstado));
        cargarUsuarios();
    }

    private void seleccionarClienteParaContratar(UsuarioTablaViewModel fila) {
        cedulaParaContratar = fila.getCedula();
        lblClienteContratar.setText(fila.getCedula() + " — " + fila.getNombre());

        List<TarifaDetalleDTORetorno> disponibles = adminUsuarios.listarServiciosDisponibles(cedulaParaContratar);
        cmbServicioContratar.getItems().setAll(disponibles.stream().map(AdminTarifasMapperUI::dtoAViewModel).toList());
        cmbServicioContratar.setValue(null);
        cmbServicioContratar.setDisable(disponibles.isEmpty());
        cmbServicioContratar.setPromptText(disponibles.isEmpty() ? "Ya tiene todos los servicios contratados" : "Selecciona servicio");
    }

    private void limpiarSeleccionContratar() {
        cedulaParaContratar = null;
        lblClienteContratar.setText("Selecciona un cliente con el botón Contratar de la lista");
        cmbServicioContratar.getItems().clear();
        cmbServicioContratar.setValue(null);
        cmbServicioContratar.setDisable(false);
        cmbServicioContratar.setPromptText("Selecciona servicio");
    }

    @FXML
    private void onContratarServicio(ActionEvent event) {
        if (cedulaParaContratar == null) {
            mostrarError("Selecciona un cliente de la lista con el botón Contratar");
            event.consume();
            return;
        }

        ServicioCatalogoViewModel servicio = cmbServicioContratar.getValue();
        if (servicio == null) {
            mostrarError("Selecciona un servicio");
            event.consume();
            return;
        }

        try {
            adminUsuarios.contratarServicio(new ContratarServicioDTOEntrada(cedulaParaContratar, servicio.getId()));
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
            event.consume();
            return;
        }

        limpiarSeleccionContratar();
        cargarResumen();
        event.consume();
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        String cedula = txtCedulaForm.getText().trim();
        String nombre = txtNombreForm.getText().trim();
        String email = txtEmailForm.getText().trim();
        String celular = txtCelularForm.getText().trim();

        if (cedula.isEmpty() || nombre.isEmpty() || email.isEmpty() || celular.isEmpty()) {
            mostrarError("Completa todos los campos");
            event.consume();
            return;
        }

        UsuarioDTOEntrada dto = new UsuarioDTOEntrada(cedula, nombre, email, celular);

        try {
            if (editando) {
                adminUsuarios.editarUsuario(dto);
                adminUsuarios.cambiarEstado(new CambiarEstadoUsuarioDTOEntrada(cedula, "Activo".equals(cmbEstadoForm.getValue())));
            } else {
                adminUsuarios.crearUsuario(dto);
            }
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
            event.consume();
            return;
        }

        limpiarFormulario();
        cargarUsuarios();
        event.consume();
    }

    @FXML
    private void onCancelar(ActionEvent event) {
        limpiarFormulario();
        event.consume();
    }

    private void limpiarFormulario() {
        editando = false;
        txtCedulaForm.clear();
        txtCedulaForm.setEditable(true);
        txtNombreForm.clear();
        txtEmailForm.clear();
        txtCelularForm.clear();
        cmbEstadoForm.setValue("Activo");
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        Navegador.estilizarDialogo(alerta);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
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

    private void cambiarModo(String modo) {
        BdService.configurarBaseDatos(modo);
        cargarResumen();
        cargarUsuarios();
        actualizarEstiloToggle(modo);
    }

    private void actualizarEstiloToggle(String modoActivo) {
        boolean sqlite = "SQLite".equalsIgnoreCase(modoActivo);
        btnSQLite.setStyle(estiloToggle(sqlite) + " -fx-background-radius: 5 0 0 5;");
        btnBinario.setStyle(estiloToggle(!sqlite) + " -fx-background-radius: 0 5 5 0;");
    }

    private String estiloToggle(boolean activo) {
        return activo
                ? "-fx-background-color: #1e1c0f; -fx-text-fill: #e8c96d; -fx-border-color: transparent; -fx-font-size: 15; -fx-padding: 5 15 5 15; -fx-cursor: hand;"
                : "-fx-background-color: #161616; -fx-text-fill: #555555; -fx-border-color: transparent; -fx-font-size: 15; -fx-padding: 5 15 5 15; -fx-cursor: hand;";
    }

    @FXML private void onUsuarios(ActionEvent event) { Navegador.irA("views/admin/adminUsuarios.fxml"); event.consume(); }
    @FXML private void onTarifas(ActionEvent event) { Navegador.irA("views/admin/adminTarifas.fxml"); event.consume(); }
    @FXML private void onCortes(ActionEvent event) { Navegador.irA("views/admin/adminCortes.fxml"); event.consume(); }
    @FXML private void onEmpresas(ActionEvent event) { Navegador.irA("views/admin/adminEmpresas.fxml"); event.consume(); }
    @FXML private void onSalir(ActionEvent event) { Navegador.irA("views/admin/loginAdmin.fxml"); event.consume(); }
}
