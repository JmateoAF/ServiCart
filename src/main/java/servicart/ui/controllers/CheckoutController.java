package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CheckoutController {
    @FXML
    private VBox listaItemsResumen;
    @FXML private Label lblTotalFinal;
    @FXML private ComboBox<String> cmbMetodoPago;  // o ComboBox<ModalidadPago>
    @FXML private TextField txtReferenciaPago;
    @FXML private Label lblMensaje;
    @FXML private Button btnCancelar;
    @FXML private Button btnConfirmarPago;

    @FXML
    public void initialize() {
        cmbMetodoPago.getItems().setAll(
                "Tarjeta de crédito",
                "Tarjeta de débito",
                "PayPal",
                "Transferencia bancaria",
                "Débito automático"
        );
    }
    public void onCancelar(ActionEvent actionEvent) {
    }

    public void onConfirmarPago(ActionEvent actionEvent) {
    }
}
