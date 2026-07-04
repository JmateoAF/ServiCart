package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import servicart.data.FactoryDAO;
import servicart.domain.services.LoginAdminImp;
import servicart.domain.services.BdService;

public class LoginAdminController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    @FXML
    private void onIniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtPassword.getText();

        // 1) Se inicializan ambas bases (admin no elige, por ahora)
        BdService.configurarBaseDatos("SQLite");
        BdService.configurarBaseDatos("Binario");

        // 2) Recién aquí se pide el DAO, ya con baseDatosActual seteada
        LoginAdminImp loginAdminImp = new LoginAdminImp(FactoryDAO.getAdminDAO());

        // 3) Dominio valida
        if (loginAdminImp.validarLogin(usuario, contrasenia)) {
            Navegador.irA("views/admin/home.fxml");
        } else {
            mostrarError("Credenciales inválidas");
        }
        event.consume();
    }

    @FXML
    private void irALoginCliente(ActionEvent event) {
        Navegador.irA("views/cliente/loginCliente.fxml");
        event.consume();
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }
}