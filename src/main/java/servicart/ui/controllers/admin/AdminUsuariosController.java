package servicart.ui.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import servicart.domain.dtos.entradas.CambiarEstadoUsuarioDTOEntrada;
import servicart.domain.dtos.entradas.UsuarioDTOEntrada;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;
import servicart.domain.interfaces.AdminUsuarios;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.AdminUsuariosMapperUI;
import servicart.ui.viewmodels.admin.UsuarioTablaViewModel;

import java.util.List;

public class AdminUsuariosController {
    @FXML private TextField txtBuscar;
    @FXML private Label lblConteo;
    @FXML private TableView<UsuarioTablaViewModel> tblUsuarios;
    @FXML private TableColumn<UsuarioTablaViewModel, String> colCedula;
    @FXML private TableColumn<UsuarioTablaViewModel, String> colNombre;
    @FXML private TableColumn<UsuarioTablaViewModel, String> colEmail;
    @FXML private TableColumn<UsuarioTablaViewModel, String> colTelefono;
    @FXML private TableColumn<UsuarioTablaViewModel, String> colEstado;
    @FXML private TableColumn<UsuarioTablaViewModel, Void> colAcciones;

    @FXML private TextField txtCedulaForm;
    @FXML private TextField txtNombreForm;
    @FXML private TextField txtEmailForm;
    @FXML private TextField txtCelularForm;
    @FXML private ComboBox<String> cmbEstadoForm;

    private final AdminUsuarios adminUsuarios;
    private boolean editando = false;

    public AdminUsuariosController(AdminUsuarios adminUsuarios) { this.adminUsuarios = adminUsuarios; }

    @FXML
    public void initialize() {
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("celular"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colAcciones.setCellFactory(columna -> crearCeldaAcciones());

        cmbEstadoForm.getItems().setAll("Activo", "Inactivo");

        txtBuscar.textProperty().addListener((obs, viejo, nuevo) -> cargarUsuarios());

        limpiarFormulario();
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        List<UsuarioDTORetorno> dtos = adminUsuarios.listarUsuarios(txtBuscar.getText());
        List<UsuarioTablaViewModel> filas = dtos.stream().map(AdminUsuariosMapperUI::dtoAFila).toList();

        tblUsuarios.getItems().setAll(filas);
        lblConteo.setText(filas.size() + " registros");
    }

    private TableCell<UsuarioTablaViewModel, Void> crearCeldaAcciones() {
        return new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEstado = new Button();
            private final HBox contenedor = new HBox(5, btnEditar, btnEstado);

            {
                btnEditar.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #e8c96d; -fx-border-color: #2e2e2e; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 15; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
                btnEditar.setOnAction(event -> { cargarEnFormulario(filaActual()); event.consume(); });
                btnEstado.setOnAction(event -> { onCambiarEstado(filaActual()); event.consume(); });
            }

            private UsuarioTablaViewModel filaActual() { return getTableView().getItems().get(getIndex()); }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }

                boolean activo = "Activo".equals(filaActual().getActivo());
                btnEstado.setText(activo ? "Desactivar" : "Activar");
                btnEstado.setStyle(activo
                        ? "-fx-background-color: #2a1010; -fx-text-fill: #c0392b; -fx-border-color: #3a1a1a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 15; -fx-padding: 5 10 5 10; -fx-cursor: hand;"
                        : "-fx-background-color: #0f2a18; -fx-text-fill: #27ae60; -fx-border-color: #1a3e28; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 15; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
                setGraphic(contenedor);
            }
        };
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

    @FXML
    private void onNuevoUsuario(ActionEvent event) {
        limpiarFormulario();
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
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML private void onDashboard(ActionEvent event) { Navegador.irA("views/admin/adminDashboard.fxml"); event.consume(); }
    @FXML private void onUsuarios(ActionEvent event) { Navegador.irA("views/admin/adminUsuarios.fxml"); event.consume(); }
    @FXML private void onTarifas(ActionEvent event) { Navegador.irA("views/admin/adminTarifas.fxml"); event.consume(); }
    @FXML private void onCortes(ActionEvent event) { Navegador.irA("views/admin/adminCortes.fxml"); event.consume(); }
    @FXML private void onSalir(ActionEvent event) { Navegador.irA("views/admin/loginAdmin.fxml"); event.consume(); }
}
