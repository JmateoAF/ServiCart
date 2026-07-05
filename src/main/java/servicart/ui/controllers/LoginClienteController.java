package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import servicart.domain.dtos.LoginClienteDTOEntrada;
import servicart.domain.dtos.LoginClienteDTOSalida;
import servicart.domain.interfaces.LoginCliente;
import servicart.domain.services.BdService;
import servicart.ui.mappers.LoginMapperUI;
import servicart.ui.viewmodels.LoginViewModel;

public class LoginClienteController {
    @FXML private TextField txtCedula;
    @FXML private ComboBox<String> cmbBaseDatos;
    @FXML private Label lblError;

    private final LoginCliente loginCliente;

    public LoginClienteController(LoginCliente loginCliente) {
        this.loginCliente = loginCliente;
    }

    @FXML
    public void initialize() {
        cmbBaseDatos.getItems().setAll("SQLite", "Binario");
        cmbBaseDatos.setValue("SQLite");
    }

    @FXML
    private void onBuscarDeudas(ActionEvent event) {
        String cedula = txtCedula.getText().trim();
        String baseDatos = cmbBaseDatos.getValue();

        if (!validarCedula(cedula)) {
            mostrarError("Número de cedula no válida");
            event.consume();
            return;
        }

        BdService.configurarBaseDatos(baseDatos);

        LoginViewModel viewModel = new LoginViewModel();
        viewModel.setCedula(cedula);
        viewModel.setBaseDatos(baseDatos);

        LoginClienteDTOEntrada dtoEntrada = LoginMapperUI.viewModelADTO(viewModel);
        LoginClienteDTOSalida dtoSalida = loginCliente.validarLoginCliente(dtoEntrada);

        if (dtoSalida == null) {
            mostrarError("Usuario no encontrado");
        } else if (dtoSalida.activo() == 0) {
            mostrarError("El usuario está inactivo");
        } else {
            Navegador.irA("views/cliente/panelCliente.fxml");
        }

        event.consume();
    }

    private boolean validarCedula(String cedula) {
        if (cedula.isEmpty()) {
            mostrarError("Ingrese su número de cédula");
            return false;
        }
        if (cedula.length() != 10) {
            mostrarError("La cédula debe tener 10 dígitos");
            return false;
        }
        try {
            int provincia = Integer.parseInt(cedula.substring(0, 2)), suma = 0, total, valor;
            int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));
            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};

            if (provincia < 1 || provincia > 24) return false;

            for (int i = 0; i < coeficientes.length; i++) {
                valor = Integer.parseInt(cedula.substring(i, i + 1)) * coeficientes[i];
                suma += (valor > 9) ? (valor - 9) : valor;
            }
            total = (suma % 10 == 0) ? 0 : (10 - (suma % 10));
            return total == digitoVerificador;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    private void irASobreNosotros(ActionEvent event) {
        Navegador.irA("views/cliente/sobreNosotros.fxml");
        event.consume();
    }

    @FXML
    private void irALoginAdmin(ActionEvent event) {
        Navegador.irA("views/admin/loginAdmin.fxml");
        event.consume();
    }

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
            e.consume();
        });
        pause.play();
    }
}