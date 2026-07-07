package servicart.ui.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import servicart.data.FactoryDAO;
import servicart.domain.services.admin.LoginAdminImp;
import servicart.domain.services.BdService;
import servicart.ui.controllers.Navegador;

public class LoginAdminController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    @FXML
    private void onIniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtPassword.getText();

        BdService.configurarBaseDatos("SQLite");
        BdService.configurarBaseDatos("Binario");

        LoginAdminImp loginAdminImp = new LoginAdminImp(FactoryDAO.getAdminDAO());

        if (loginAdminImp.validarLogin(usuario, contrasenia)) {
            Navegador.irA("views/admin/home.fxml");
        } else {
            mostrarError();
        }
        event.consume();
    }

    @FXML
    private void irALoginCliente(ActionEvent event) {
        Navegador.irA("views/cliente/loginCliente.fxml");
        event.consume();
    }

    private void mostrarError() {
        lblError.setText("Credenciales inválidas");
        lblError.setPrefHeight(33);
        javafx.scene.layout.VBox.setMargin(lblError, new javafx.geometry.Insets(5, 0, 15, 0));
        lblError.setVisible(true);

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
        pause.setOnFinished(event -> {
            lblError.setVisible(false);
            lblError.setPrefHeight(0);
            javafx.scene.layout.VBox.setMargin(lblError, new javafx.geometry.Insets(0));
            event.consume();
        });
        pause.play();
    }
}