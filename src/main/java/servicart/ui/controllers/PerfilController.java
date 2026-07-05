package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import servicart.ui.viewmodels.cliente.PerfilClienteViewModel;

public class PerfilController {
    @FXML private VBox listaServicios;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblEmailUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCelular;
    @FXML private TextField txtCedula;
    @FXML private Label lblMensaje;

    private PerfilClienteViewModel clienteVM;
    private String baseDatos;

    @FXML
    public void initialize() {
        this.clienteVM = Navegador.getClientePendiente();
        this.baseDatos = Navegador.getBaseDatosPendiente();
        cargarDatos();
    }

    private void cargarDatos() {
        lblNombreUsuario.setText(clienteVM.getNombre());
        lblEmailUsuario.setText(clienteVM.getEmail());
        txtNombre.setText(clienteVM.getNombre());
        txtCedula.setText(clienteVM.getCedula());
        txtEmail.setText(clienteVM.getEmail());
        txtCelular.setText(clienteVM.getTelefono());
    }
    // Solo el admin edita
/*
    @FXML
    private void onGuardarDatos(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String celular = txtCelular.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || celular.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios");

            return;
        }

    }
*/
    @FXML private void onMisServicios(ActionEvent e) { Navegador.irA("views/cliente/panelCliente.fxml"); }
    @FXML private void onCarrito(ActionEvent e) { Navegador.irA("views/cliente/carrito.fxml"); }
/*
    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setVisible(true);
    }

 */
@FXML private void onSalir(ActionEvent e) { Navegador.irA("views/cliente/loginCliente.fxml"); }

}