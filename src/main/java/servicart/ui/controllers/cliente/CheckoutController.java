package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import servicart.domain.dtos.entradas.CarritoDTOEntrada;
import servicart.domain.dtos.entradas.ConfirmarPagoDTOEntrada;
import servicart.domain.dtos.retornos.CarritoDTORetorno;
import servicart.domain.interfaces.Checkout;
import servicart.entities.enums.ModalidadPago;
import servicart.ui.SesionCliente;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.CarritoMapperUI;
import servicart.ui.viewmodels.cliente.AbonoCarritoViewModel;
import servicart.ui.viewmodels.cliente.CarritoResumenViewModel;

public class CheckoutController {
    @FXML private VBox listaItemsResumen;
    @FXML private Label lblTotalFinal;
    @FXML private ComboBox<String> cmbMetodoPago;
    @FXML private TextField txtReferenciaPago;
    @FXML private Label lblMensaje;
    @FXML private Button btnConfirmarPago;

    private final Checkout checkout;

    public CheckoutController(Checkout checkout) { this.checkout = checkout; }

    @FXML
    public void initialize() {
        cmbMetodoPago.getItems().setAll("Tarjeta de crédito", "Tarjeta de débito", "PayPal", "Transferencia bancaria", "Débito automático");
        cmbMetodoPago.setValue("Tarjeta de crédito");
        cargarResumen();
    }

    private void cargarResumen() {
        CarritoDTORetorno dto = checkout.obtenerResumen(new CarritoDTOEntrada(SesionCliente.getCedulaActual()));
        CarritoResumenViewModel vm = CarritoMapperUI.DTOAviewModel(dto);

        listaItemsResumen.getChildren().clear();
        vm.getItems().forEach(item -> listaItemsResumen.getChildren().add(crearFilaItem(item)));
        lblTotalFinal.setText(vm.getTotal());

        boolean vacio = vm.getItems().isEmpty();
        btnConfirmarPago.setDisable(vacio);
        if (vacio) mostrarMensaje("Tu carrito está vacío, no hay nada que pagar");
    }

    private HBox crearFilaItem(AbonoCarritoViewModel item) {
        Label servicio = new Label(item.getServicio());
        servicio.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13;");
        HBox espaciador = new HBox();
        HBox.setHgrow(espaciador, Priority.ALWAYS);
        Label monto = new Label(item.getMonto());
        monto.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 13;");
        return new HBox(8, servicio, espaciador, monto);
    }

    @FXML
    private void onConfirmarPago(ActionEvent event) {
        if (cmbMetodoPago.getValue() == null) { mostrarMensaje("Seleccione un método de pago"); event.consume(); return; }
        if (txtReferenciaPago.getText() == null || txtReferenciaPago.getText().isBlank()) { mostrarMensaje("Ingresa una referencia de pago"); event.consume(); return; }

        try {
            ModalidadPago modalidad = modalidadDesdeCombo(cmbMetodoPago.getValue());
            checkout.confirmarPago(new ConfirmarPagoDTOEntrada(SesionCliente.getCedulaActual(), modalidad));
            Navegador.irA("views/cliente/panelCliente.fxml");
        } catch (Exception ex) {
            mostrarMensaje("No se pudo procesar el pago: " + ex.getMessage());
        }
        event.consume();
    }

    private ModalidadPago modalidadDesdeCombo(String valor) {
        return switch (valor) {
            case "Tarjeta de crédito" -> ModalidadPago.TC;
            case "Tarjeta de débito" -> ModalidadPago.TD;
            case "PayPal" -> ModalidadPago.PAYPAL;
            case "Transferencia bancaria" -> ModalidadPago.TRANSFERENCIA;
            default -> ModalidadPago.DEBITO;
        };
    }

    @FXML
    private void onCancelar(ActionEvent event) {
        Navegador.irA("views/cliente/carrito.fxml");
        event.consume();
    }

    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setVisible(true);
        lblMensaje.setManaged(true);
    }
}