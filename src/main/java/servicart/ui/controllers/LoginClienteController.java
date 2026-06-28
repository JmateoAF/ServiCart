package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import servicart.data.FactoryDAO;
import servicart.data.FactoryDAO.Estrategia;
import servicart.domain.models.entities.Cliente;
import servicart.domain.services.ClienteServices;
import servicart.exceptions.ServiCartException;
import servicart.ui.core.GestorTema;
import servicart.ui.core.Navegador;
import servicart.ui.core.Sesion;

import java.util.Optional;

public class LoginClienteController {
    @FXML private Button btnTema;
    @FXML private TextField txtCedula;
    @FXML private ComboBox<String> cmbBaseDatos;
    @FXML private Button btnBuscar;
    @FXML private Label lblError;

    @FXML
    public void initialize() {
        cmbBaseDatos.getItems().setAll("SQLite", "Binario");
        cmbBaseDatos.setValue("SQLite");
        lblError.setVisible(false);
        GestorTema.configurar(btnTema);
    }

    @FXML
    private void onBuscarDeudas(ActionEvent event) {
        String cedula = txtCedula.getText().trim();

        if (cedula.isEmpty()) {
            mostrarError("Ingrese su número de cédula");

            return;
        }
        if (!cedula.matches("\\d{10}")) {
            mostrarError("La cédula debe tener 10 dígitos");

            return;
        }

        //Configurar la base de datos según elección del usuario
        Estrategia estrategia = "SQLite".equals(cmbBaseDatos.getValue()) ? Estrategia.SQLITE : Estrategia.BINARIO;
        FactoryDAO.configurar(estrategia);

        // Buscar cliente en la base de datos elegida
        try {
            ClienteServices clienteService = new ClienteServices(FactoryDAO.clienteDAO());
            Optional<Cliente> resultado   = clienteService.buscarId(cedula);

            if (resultado.isEmpty()) {
                mostrarError("No se encontró ningún cliente con esa cédula");

                return;
            }

            //Iniciar sesión y navegar al panel
            Sesion.iniciar(resultado.get());
            Navegador.irA("views/cliente/panelCliente.fxml");
        } catch (ServiCartException e) {
            mostrarError("Error al conectar con la base de datos");
        }
    }

    @FXML
    private void irASobreNosotros(ActionEvent event) {
        Navegador.irA("views/cliente/sobreNosotros.fxml");
    }

    @FXML
    private void irALoginAdmin(ActionEvent event) {
        Navegador.irA("views/admin/loginAdmin.fxml");
    }

    @FXML
    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }
}