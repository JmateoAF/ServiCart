package servicart.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class LoginClienteController {

    // --------------------------------------------------
    // Nodos inyectados desde el FXML
    // --------------------------------------------------
    @FXML private TextField txtCedula;
    @FXML private Label lblError;
    @FXML private Button btnBuscar;

    // --------------------------------------------------
    // Navegación
    // --------------------------------------------------
    @FXML
    private void irASobreNosotros(ActionEvent event) {
        cambiarVista(event, "views/cliente/sobreNosotros.fxml");
    }

    @FXML
    private void irALoginAdmin(ActionEvent event) {
        cambiarVista(event, "views/admin/loginAdmin.fxml");
    }

    // --------------------------------------------------
    // Acción principal: buscar cliente por cédula
    // --------------------------------------------------
    @FXML
    private void onBuscarDeudas(ActionEvent event) {
        String cedula = txtCedula.getText().trim();

        // Validación básica
        if (cedula.isEmpty()) {
            mostrarError("Debe ingresar un número de cédula.");
            return;
        }
        if (!cedula.matches("\\d+")) {
            mostrarError("La cédula debe contener solo dígitos.");
            return;
        }

        // Aquí buscarías al cliente en la base de datos.
        // Si existe, cargas panelCliente y le pasas el cliente.
        // Si no existe, muestras error.

        // Ejemplo: si el cliente no se encuentra
        // mostrarError("Cliente no encontrado.");
        // return;

        // Si se encontró, ir al panel
        cambiarVista(event, "views/cliente/panelCliente.fxml");
    }

    // --------------------------------------------------
    // Métodos auxiliares
    // --------------------------------------------------
    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    private void cambiarVista(ActionEvent event, String rutaFXML) {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(ClassLoader.getSystemResource(rutaFXML))
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error al cambiar de vista: " + e.getMessage());
        }
    }
}