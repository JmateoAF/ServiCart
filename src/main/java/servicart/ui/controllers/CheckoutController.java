package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import servicart.data.FactoryDAO;
import servicart.models.entities.Abono;
import servicart.models.entities.Carrito;
import servicart.models.enums.ModalidadPago;
import servicart.domain.services.CheckoutService;
import servicart.exceptions.ServiCartException;
import servicart.ui.core.GestorNotificacion;
import servicart.ui.core.Navegador;
import servicart.ui.core.Sesion;

public class CheckoutController {
    @FXML private VBox listaResumen;
    @FXML private Label lblTotalFinal;
    @FXML private ComboBox<String> cmbMetodoPago;
    @FXML private Label lblMensaje;
    private CheckoutService checkoutService;

    @FXML
    public void initialize() {
        //checkoutService = new CheckoutService(FactoryDAO.abonoDAO(), GestorNotificacion.getFacturacionService());
        cmbMetodoPago.getItems().setAll("Tarjeta de crédito", "Tarjeta de débito", "PayPal", "Transferencia bancaria", "Débito automático");
        cmbMetodoPago.setValue("Tarjeta de crédito");
        cargarResumen();
    }

    private void cargarResumen() {
        Carrito carrito = Sesion.getCarrito();
        listaResumen.getChildren().clear();

        for (Abono abono : carrito.getAbonos()) {
            //String empresa = abono.getFactura().getContrato().getServicio().getEmpresa().name();
            String tipo = abono.getFactura().getContrato().getServicio().getTipo().name();
            //Label fila = new Label("• " + empresa + " – " + tipo + "  →  $" + String.format("%.2f", abono.getMonto()));
           // listaResumen.getChildren().add(fila);
        }

        lblTotalFinal.setText("Total: $" + String.format("%.2f", carrito.getTotal()));
        lblMensaje.setVisible(false);
    }

    @FXML
    private void onConfirmarPago(ActionEvent event) {
        if (cmbMetodoPago.getValue() == null) {
            mostrarMensaje("Seleccione un método de pago");

            return;
        }

        //Asignar modalidad a cada abono según la selección
        /*ModalidadPago modalidad = modalidadDesdeCombo(cmbMetodoPago.getValue());
        for(Abono abono : Sesion.getCarrito().getAbonos()) {
            // La modalidad se asignó al crear el Abono, pero si se cambia aquí se puede hacer via setter
        }*/

        try {
            checkoutService.procesarPago(Sesion.getCarrito());
            mostrarMensaje("✓ Pago realizado con éxito.");
            //Después de pagar, volver al panel
            Navegador.irA("views/cliente/panelCliente.fxml");
        } catch (ServiCartException e) {
            mostrarMensaje("Error al procesar el pago: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelar(ActionEvent event) { Navegador.irA("views/cliente/carrito.fxml"); }

    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setVisible(true);
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
}