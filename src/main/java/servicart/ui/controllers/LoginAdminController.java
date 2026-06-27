package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import servicart.data.FactoryDAO;
import servicart.domain.services.AdminServices;
import servicart.exceptions.ServiCartException;
import servicart.ui.Navegador;

public class LoginAdminController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private final AdminServices adminService = new AdminServices(FactoryDAO.adminDAO());

    @FXML
    public void initialize() {
        lblError.setVisible(false);
    }

    @FXML
    private void onIniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtPassword.getText();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mostrarError("Ingrese usuario y contraseña");

            return;
        }

        try {
            if (adminService.validarLogin(usuario, contrasenia)) Navegador.irA("views/admin/panelAdmin.fxml");
            else mostrarError("Usuario o contraseña incorrectos.");
        } catch (ServiCartException e) {
            mostrarError("Error al conectar con la base de datos.");
        }
    }

    @FXML
    private void irALoginCliente(ActionEvent event) {
        Navegador.irA("views/cliente/loginCliente.fxml");
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }
}
