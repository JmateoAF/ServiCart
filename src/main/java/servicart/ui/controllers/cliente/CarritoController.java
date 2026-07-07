package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import servicart.ui.SesionCliente;
import servicart.ui.controllers.Navegador;

public class CarritoController {
    @FXML public Label lblMensaje;

    @FXML
    private void onMisServicios(ActionEvent event) {
        Navegador.irA("views/cliente/panelCliente.fxml");
        event.consume();
    }

    @FXML
    private void onPerfil(ActionEvent event) {
        Navegador.irA("views/cliente/perfilCliente.fxml");
        event.consume();
    }

    @FXML
    private void onSalir(ActionEvent event) {
        SesionCliente.cerrar();
        Navegador.irA("views/cliente/loginCliente.fxml");
        event.consume();
    }

    @FXML
    private void onVaciarCarrito(ActionEvent event) {
        event.consume();
    }

    @FXML
    private void onCheckout(ActionEvent event) {
        Navegador.irA("views/cliente/checkout.fxml");
        event.consume();
    }
}