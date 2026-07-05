package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import servicart.ui.controllers.Navegador;

public class CarritoController {
    @FXML public Label lblMensaje;
    @FXML private VBox listaItems;
    @FXML private VBox paneVacio;
    @FXML private Label lblCantidad;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTotalFinal;

    /*@FXML
    public void initialize() { cargarCarrito(); }

    private void cargarCarrito() {
        listaItems.getChildren().clear();

        if (carrito.estaVacio()) {
            paneVacio.setVisible(true);
            listaItems.setVisible(false);
            btnCheckout.setDisable(true);

            return;
        }

        paneVacio.setVisible(false);
        listaItems.setVisible(true);

        for (Abono abono : carrito.getAbonos()) listaItems.getChildren().add(crearFilaItem(abono));

        lblCantidad.setText(carrito.cantidadItems() + " ítem(s)");
        lblSubtotal.setText("$" + String.format("%.2f", carrito.getTotal()));
        lblTotalFinal.setText("$" + String.format("%.2f", carrito.getTotal()));
    }

    private HBox crearFilaItem(Abono abono) {
        HBox fila = new HBox(10);
        fila.setPadding(new Insets(6));

        String descripcion = abono.getDescripcionServicio();

        Label lblDesc  = new Label(descripcion);
        Label lblMonto = new Label("$" + String.format("%.2f", abono.getMonto()));

        Button btnEliminar = new Button("✕");
        btnEliminar.setOnAction(e -> {

            cargarCarrito();
        });

        fila.getChildren().addAll(lblDesc, lblMonto, btnEliminar);
        return fila;
    }*/

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

    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setVisible(true);
    }
}