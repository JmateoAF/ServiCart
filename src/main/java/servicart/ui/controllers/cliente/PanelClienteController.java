// ui.controllers.cliente.PanelClienteController
package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import servicart.domain.dtos.entradas.AgregarAbonoDTOEntrada;
import servicart.domain.dtos.entradas.PanelClienteDTOEntrada;
import servicart.domain.dtos.salidas.ServicioContratadoDTOSalida;
import servicart.domain.interfaces.PanelCliente;
import servicart.ui.SesionCliente;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.PanelClienteMapperUI;
import servicart.ui.viewmodels.cliente.FacturaPendienteViewModel;
import servicart.ui.viewmodels.cliente.ServicioContratadoViewModel;

import java.util.List;

public class PanelClienteController {
    @FXML private VBox contenedorServicios;
    @FXML private Label lblMensaje;

    private final PanelCliente panelCliente;

    public PanelClienteController(PanelCliente panelCliente) {
        this.panelCliente = panelCliente;
    }

    @FXML
    public void initialize() { cargarServicios(); }

    private void cargarServicios() {
        String cedula = SesionCliente.getCedulaActual();
        List<ServicioContratadoDTOSalida> dtos = panelCliente.listarServiciosContratados(new PanelClienteDTOEntrada(cedula));
        List<ServicioContratadoViewModel> vms = dtos.stream().map(PanelClienteMapperUI::DTOAviewModel).toList();

        contenedorServicios.getChildren().clear();

        if (vms.isEmpty()) {
            lblMensaje.setText("No tienes servicios contratados");
            return;
        }
        lblMensaje.setText("");
        vms.forEach(vm -> contenedorServicios.getChildren().add(crearTarjetaServicio(vm)));
    }

    private VBox crearTarjetaServicio(ServicioContratadoViewModel vm) {
        VBox tarjeta = new VBox(8);
        tarjeta.setStyle("-fx-background-color: #161616; -fx-border-color: #252525; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 15;");

        Label titulo = new Label(vm.getNombreServicio() + " – " + vm.getEmpresa());
        titulo.setStyle("-fx-text-fill: #e8c96d; -fx-font-size: 18; -fx-font-family: 'Courier New';");

        Label estado = new Label("Estado: " + vm.getEstadoContrato());
        estado.setStyle("-fx-text-fill: #888888;");

        Label deuda = new Label("Deuda total: " + vm.getDeudaTotal());
        deuda.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");

        tarjeta.getChildren().addAll(titulo, estado, deuda);
        vm.getListaFacturas().forEach(f -> tarjeta.getChildren().add(crearFilaFactura(f)));
        return tarjeta;
    }

    private HBox crearFilaFactura(FacturaPendienteViewModel factura) {
        HBox fila = new HBox(10);
        fila.setStyle("-fx-alignment: center-left;");

        Label info = new Label("Factura #" + factura.getIdFactura() + " – " + factura.getMonto() + " – vence " + factura.getFechaVencimiento());
        info.setStyle("-fx-text-fill: #aaaaaa;");
        HBox.setHgrow(info, Priority.ALWAYS);

        Button btn = new Button("Agregar al carrito");
        btn.setStyle("-fx-background-color: #e8c96d; -fx-text-fill: #0f0f0f; -fx-cursor: hand;");
        btn.setOnAction(e -> onAgregarAlCarrito(factura));

        fila.getChildren().addAll(info, btn);
        return fila;
    }

    private void onAgregarAlCarrito(FacturaPendienteViewModel factura) {
        String cedula = SesionCliente.getCedulaActual();
        double monto = Double.parseDouble(factura.getMonto().replace("$", "").trim());
        panelCliente.agregarAbonoAlCarrito(new AgregarAbonoDTOEntrada(cedula, factura.getIdFactura(), monto));
        cargarServicios();
    }

    @FXML private void onCarrito(ActionEvent e) { Navegador.irA("views/cliente/carritoCliente.fxml"); e.consume(); }
    @FXML private void onPerfil(ActionEvent e) { Navegador.irA("views/cliente/perfilCliente.fxml"); e.consume(); }
    @FXML private void onSalir(ActionEvent e) { SesionCliente.cerrar(); Navegador.irA("views/cliente/loginCliente.fxml"); e.consume(); }
}