package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import servicart.domain.interfaces.LoginAdmin;
import servicart.domain.services.AdminServices;

public class LoginAdminController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    private final LoginAdmin loginService;

    public LoginAdminController() {
        this.loginService = new AdminServices();
    }

    @FXML
    private void iniciarSesion(ActionEvent event){
        String usuario = txtUsuario.getText();
        String contrasenia = txtPassword.getText();

        boolean esValido = loginService.validarLogin(usuario, contrasenia);

        if (esValido) {
            System.out.println("¡Login correcto! Bienvenido admin.");
        } else {
            System.out.println("Credenciales incorrectas o campos vacíos.");
        }
    }

    @FXML
    private void irALoginCliente(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource("views/cliente/loginCliente.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error al cambiar de vista: " + e.getMessage());
        }
    }
}
