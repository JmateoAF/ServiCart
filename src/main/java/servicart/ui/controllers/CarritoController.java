package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CarritoController {

    @FXML private Label lblCantidad;
    @FXML private Label lblTotalFinal;
    @FXML private VBox contenedorTabla;
    @FXML private VBox listaItems;
    @FXML private VBox paneVacio;
    @FXML private Label lblSubtotal;
    @FXML private Label lblMora;
    @FXML private Button btnCheckout;


    @FXML
    public void initialize() {
        // Cargar datos del carrito (desde algún modelo compartido)
        // Actualizar lblCantidad y lblTotalFinal
    }

    @FXML
    private void onMisServicios(ActionEvent event) {
        // Navegar a la vista de servicios
    }

    @FXML
    private void onPerfil(ActionEvent event) {
        // Navegar al perfil
    }

    @FXML
    private void onSalir(ActionEvent event) {
        // Cerrar sesión
    }

    @FXML
    private void onEliminarItem(ActionEvent event) {
        // Obtener el botón que disparó el evento
        Button btn = (Button) event.getSource();
        // Obtener el ítem asociado (por ejemplo, desde userData)
        // Eliminar del carrito y actualizar vista
    }

    @FXML
    private void onVolverServicios(ActionEvent event) {
        // Volver a la vista de servicios (panelCliente)
    }

    @FXML
    private void onCheckout(ActionEvent event) {
        // Procesar pago
    }
}