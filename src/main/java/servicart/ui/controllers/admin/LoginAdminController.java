package servicart.ui.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import servicart.domain.dtos.entradas.LoginAdminDTOEntrada;
import servicart.domain.dtos.retornos.LoginAdminDTORetorno;
import servicart.domain.interfaces.LoginAdmin;
import servicart.domain.services.BdService;
import servicart.domain.services.empresa.GestorProcesosEmpresa;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.LoginAdminMapperUI;
import servicart.ui.viewmodels.admin.LoginAdminViewModel;

public class LoginAdminController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private final LoginAdmin loginAdmin;

    public LoginAdminController(LoginAdmin loginAdmin) { this.loginAdmin = loginAdmin; }

    @FXML
    private void onIniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtPassword.getText();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mostrarError("Ingrese usuario y contraseña");
            return;
        }

        BdService.configurarBaseDatos("SQLite");
        GestorProcesosEmpresa.iniciarSiEsNecesario();

        LoginAdminViewModel avm = new LoginAdminViewModel();
        avm.setUsuario(usuario);
        avm.setContrasenia(contrasenia);

        LoginAdminDTOEntrada dtoEntrada = LoginAdminMapperUI.viewModelADTO(avm);
        LoginAdminDTORetorno dtoRetorno = loginAdmin.validarLoginAdmin(dtoEntrada);

        if(dtoRetorno == null) {
            mostrarError("Usuario o contraseña incorrectos");
            event.consume();
            return;
        }

        avm = LoginAdminMapperUI.DTOAviewModel(dtoRetorno);

        if(usuario.equals(avm.getUsuario()) && contrasenia.equals(avm.getContrasenia())) {
            Navegador.irA("views/admin/adminUsuarios.fxml");
        } else {
            mostrarError("Ingrese sus credenciales correctamente");
            return;
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