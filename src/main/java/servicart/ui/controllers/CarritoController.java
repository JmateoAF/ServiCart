package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import servicart.models.entities.Abono;
import servicart.models.entities.Carrito;
import servicart.ui.Navegador;

public class CarritoController {
    @FXML private VBox  listaItems;
    @FXML private VBox  paneVacio;
    @FXML private Label lblCantidad;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTotalFinal;
    @FXML private Button btnCheckout;

    @FXML
    public void initialize() {
        cargarCarrito();
    }

    private void cargarCarrito() {
        listaItems.getChildren().clear();

/*        if (carrito.estaVacio()) {
            paneVacio.setVisible(true);
            listaItems.setVisible(false);
            btnCheckout.setDisable(true);

            return;
        }*/

        paneVacio.setVisible(false);
        listaItems.setVisible(true);
        btnCheckout.setDisable(false);

   /*     for (Abono abono : carrito.getAbonos()) listaItems.getChildren().add(crearFilaItem(abono));

        lblCantidad.setText(carrito.cantidadItems() + " ítem(s)");
        lblSubtotal.setText("$" + String.format("%.2f", carrito.getTotal()));
        lblTotalFinal.setText("$" + String.format("%.2f", carrito.getTotal()));*/
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
    }

    @FXML
    private void onCheckout(ActionEvent event) { Navegador.irA("views/cliente/checkout.fxml"); }

    @FXML
    private void onVaciarCarrito(ActionEvent event) {

        cargarCarrito();
    }

    @FXML private void onMisServicios(ActionEvent e) { Navegador.irA("views/cliente/panelCliente.fxml"); }
    @FXML private void onPerfil(ActionEvent e) { Navegador.irA("views/cliente/perfilCliente.fxml"); }

    public void onVolverServicios(ActionEvent actionEvent)  {
        Navegador.irA("views/cliente/panelCliente.fxml");
    }
}