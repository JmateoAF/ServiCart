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