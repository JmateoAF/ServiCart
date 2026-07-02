package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import servicart.exceptions.ServiCartException;

public class LoginAdminController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    @FXML
    private void onIniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtPassword.getText();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mostrarError("Ingrese usuario y contraseña");
            return;
        }

        
        //if (adminService.validarLogin(usuario, contrasenia)) Navegador.irA("views/admin/panelAdmin.fxml");
        //else mostrarError("Usuario o contraseña incorrectos");
    }

    @FXML
    private void irALoginCliente(ActionEvent event) { Navegador.irA("views/cliente/loginCliente.fxml"); }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setPrefHeight(33);
        javafx.scene.layout.VBox.setMargin(lblError, new javafx.geometry.Insets(5, 0, 15, 0));
        lblError.setVisible(true);

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
        pause.setOnFinished(e -> {
            lblError.setVisible(false);
            lblError.setPrefHeight(0);
            javafx.scene.layout.VBox.setMargin(lblError, new javafx.geometry.Insets(0));
        });
        pause.play();
    }
}
