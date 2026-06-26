package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PerfilController {

    // ============================================================
    // Campos vinculados al FXML
    // ============================================================

    @FXML private Label lblNombreUsuario;
    @FXML private Label lblEmailUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private Label lblMensaje;
    @FXML private VBox listaServicios;

    // ============================================================
    // Métodos de navegación (sidebar)
    // ============================================================

    @FXML
    private void onMisServicios(ActionEvent event) {
        // TODO: navegar a la vista de servicios (panelCliente)
    }

    @FXML
    private void onCarrito(ActionEvent event) {
        // TODO: navegar al carrito
    }

    @FXML
    private void onSalir(ActionEvent event) {
        // TODO: cerrar sesión y volver al login
    }

    // ============================================================
    // Métodos de acciones del perfil
    // ============================================================

    @FXML
    private void onGuardarDatos(ActionEvent event) {
        // TODO: guardar cambios en los datos personales
        // Puedes obtener los valores con:
        // String nombre = txtNombre.getText();
        // String cedula = txtCedula.getText();
        // ...
        System.out.println("Guardando datos personales...");
    }

    @FXML
    private void onCambiarPassword(ActionEvent event) {
        // TODO: validar y cambiar la contraseña
        // String actual = txtPassActual.getText();
        // String nueva = txtPassNueva.getText();
        // String confirmar = txtPassConfirmar.getText();
        System.out.println("Cambiando contraseña...");
    }

    // ============================================================
    // Método de inicialización (opcional)
    // ============================================================

    @FXML
    public void initialize() {
        // Aquí puedes cargar datos reales del usuario
        // Por ejemplo:
        // lblNombreUsuario.setText("Nombre real");
        // lblEmailUsuario.setText("email@ejemplo.com");
        // txtNombre.setText("Nombre real");
        // ...
        System.out.println("PerfilController inicializado.");
    }

    public void onAgregarServicio(ActionEvent actionEvent) {

    }
}