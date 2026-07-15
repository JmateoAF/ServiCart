package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import servicart.domain.dtos.entradas.LoginClienteDTOEntrada;
import servicart.domain.dtos.retornos.LoginClienteDTORetorno;
import servicart.domain.interfaces.LoginCliente;
import servicart.domain.services.BdService;
import servicart.domain.services.empresa.GestorProcesosEmpresa;
import servicart.ui.SesionCliente;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.LoginClienteMapperUI;
import servicart.ui.viewmodels.cliente.LoginClienteViewModel;

public class LoginClienteController {
    private final LoginCliente loginCliente;
    @FXML
    private TextField txtCedula;
    @FXML
    private ComboBox<String> cmbBaseDatos;
    @FXML
    private Label lblError;

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
            event.consume();
            return;
        }

        BdService.configurarBaseDatos(baseDatos);
        GestorProcesosEmpresa.iniciarSiEsNecesario();

        LoginClienteViewModel viewModel = new LoginClienteViewModel();
        viewModel.setCedula(cedula);
        viewModel.setBaseDatos(baseDatos);

        LoginClienteDTOEntrada dtoEntrada = LoginClienteMapperUI.viewModelADTO(viewModel);
        LoginClienteDTORetorno dtoSalida = loginCliente.validarLoginCliente(dtoEntrada);

        if (dtoSalida == null) {
            mostrarError("Usuario no encontrado");
            event.consume();
            return;
        }

        viewModel = LoginClienteMapperUI.DTOAviewModel(dtoSalida);

        if (viewModel.getActivo() == 0) {
            mostrarError("El usuario está inactivo");
        } else {
            SesionCliente.iniciarSesion(cedula);
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

            if (total != digitoVerificador) {
                mostrarError("Número de cédula no válida");
                return false;
            }

            return true;
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
        pause.setOnFinished(event -> {
            lblError.setVisible(false);
            lblError.setPrefHeight(0);
            javafx.scene.layout.VBox.setMargin(lblError, new javafx.geometry.Insets(0));
            event.consume();
        });
        pause.play();
    }
}