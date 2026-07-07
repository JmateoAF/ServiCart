package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import servicart.ui.controllers.Navegador;

public class CheckoutController {
    @FXML private ComboBox<String> cmbMetodoPago;
    @FXML private Label lblMensaje;

    @FXML
    public void initialize() {
        cmbMetodoPago.getItems().setAll("Tarjeta de crédito", "Tarjeta de débito", "PayPal", "Transferencia bancaria", "Débito automático");
        cmbMetodoPago.setValue("Tarjeta de crédito");
        cargarResumen();
    }

    private void cargarResumen() {
        lblMensaje.setVisible(false);
    }

    @FXML
    private void onConfirmarPago(ActionEvent event) {
        if (cmbMetodoPago.getValue() == null) {
            mostrarMensaje("Seleccione un método de pago");

            return;
        }

        mostrarMensaje("Pago realizado con éxito");
        Navegador.irA("views/cliente/panelCliente.fxml");

        event.consume();
    }

    /*private ModalidadPago modalidadDesdeCombo(String valor) {
        return switch (valor) {
            case "Tarjeta de crédito" -> ModalidadPago.TC;
            case "Tarjeta de débito" -> ModalidadPago.TD;
            case "PayPal" -> ModalidadPago.PAYPAL;
            case "Transferencia bancaria" -> ModalidadPago.TRANSFERENCIA;
            default -> ModalidadPago.DEBITO;
        };
    }*/

    @FXML
    private void onCancelar(ActionEvent event) {
        Navegador.irA("views/cliente/carrito.fxml");
        event.consume();
    }

    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setVisible(true);
    }
}