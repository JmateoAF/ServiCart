package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import servicart.domain.dtos.entradas.CarritoDTOEntrada;
import servicart.domain.dtos.retornos.CarritoDTORetorno;
import servicart.domain.interfaces.CarritoCliente;
import servicart.ui.SesionCliente;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.CarritoMapperUI;
import servicart.ui.viewmodels.cliente.AbonoCarritoViewModel;
import servicart.ui.viewmodels.cliente.CarritoResumenViewModel;

import java.util.Optional;

public class CarritoController {
    @FXML private VBox listaItems;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTotalFinal;
    @FXML private Label lblMensaje;

    private final CarritoCliente carritoCliente;

    public CarritoController(CarritoCliente carritoCliente) { this.carritoCliente = carritoCliente; }

    @FXML
    public void initialize() { cargarCarrito(); }

    private void cargarCarrito() {
        CarritoDTORetorno dto = carritoCliente.verCarrito(new CarritoDTOEntrada(SesionCliente.getCedulaActual()));
        CarritoResumenViewModel vm = CarritoMapperUI.DTOAviewModel(dto);

        listaItems.getChildren().clear();

        if (vm.getItems().isEmpty()) {
            lblMensaje.setText("Tu carrito está vacío");
            lblSubtotal.setText("$ 0.00");
            lblTotalFinal.setText("$ 0.00");
            return;
        }

        lblMensaje.setText("");
        vm.getItems().forEach(item -> listaItems.getChildren().add(crearFilaItem(item)));
        lblSubtotal.setText(vm.getSubtotal());
        lblTotalFinal.setText(vm.getTotal());
    }

    private HBox crearFilaItem(AbonoCarritoViewModel item) {
        Label servicio = new Label(item.getServicio());
        servicio.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 15;");
        Label referencia = new Label(item.getReferenciaFactura());
        referencia.setStyle("-fx-text-fill: #555555; -fx-font-size: 12;");
        VBox textos = new VBox(2, servicio, referencia);

        Label monto = new Label(item.getMonto());
        monto.setStyle("-fx-text-fill: #e8c96d; -fx-font-weight: bold; -fx-font-size: 15;");

        HBox espaciador = new HBox();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        HBox fila = new HBox(10, textos, espaciador, monto);
        fila.setStyle("-fx-background-color: #161616; -fx-border-color: #252525; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        return fila;
    }

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
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Vaciar carrito");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que deseas quitar todos los abonos del carrito?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            carritoCliente.vaciarCarrito(new CarritoDTOEntrada(SesionCliente.getCedulaActual()));
            cargarCarrito();
        }
        event.consume();
    }

    @FXML
    private void onCheckout(ActionEvent event) {
        Navegador.irA("views/cliente/checkout.fxml");
        event.consume();
    }
}