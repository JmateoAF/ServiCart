package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import servicart.domain.services.ClienteServices;
import servicart.exceptions.ServiCartException;
import servicart.ui.Navegador;

public class PerfilController {
    public TextField txtTelefono;
    @FXML private VBox listaServicios;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblEmailUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCelular;
    @FXML private TextField txtCedula;
    @FXML private Label lblMensaje;

    private ClienteServices clienteService;

    @FXML
    public void initialize() {
        //clienteService = new ClienteServices(FactoryDAO.clienteDAO());
        cargarDatos();
    }

    private void cargarDatos() {
    }

    @FXML
    private void onGuardarDatos(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String celular = txtCelular.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || celular.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios");

            return;
        }

        try {
            mostrarMensaje("✓ Datos actualizados correctamente");
            cargarDatos();
        } catch (ServiCartException e) {
            mostrarMensaje("Error al guardar: " + e.getMessage());
        }
    }

    @FXML private void onMisServicios(ActionEvent e) { Navegador.irA("views/cliente/panelCliente.fxml"); }
    @FXML private void onCarrito(ActionEvent e) { Navegador.irA("views/cliente/carrito.fxml"); }

    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setVisible(true);
    }
}