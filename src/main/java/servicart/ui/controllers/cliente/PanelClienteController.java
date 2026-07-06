package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PanelClienteController {
    @FXML private VBox contenedorServicios;
    @FXML private Label lblMensaje;

    private final PanelCliente panelCliente;
    // Recuerda qué tarjetas quedaron expandidas, para no colapsarlas al refrescar
    private final Set<Integer> expandidos = new HashSet<>();

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

    // ---- Tarjeta colapsable por servicio ----
    private VBox crearTarjetaServicio(ServicioContratadoViewModel vm) {
        VBox tarjeta = new VBox();
        tarjeta.setStyle("-fx-background-color: #161616; -fx-border-color: #252525; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label chevron = new Label(expandidos.contains(vm.getIdContrato()) ? "▼" : "▶");
        chevron.setStyle("-fx-text-fill: #666666;");

        VBox contenidoExpandido = new VBox(10);
        contenidoExpandido.setStyle("-fx-padding: 0 15 15 15;");
        contenidoExpandido.setVisible(expandidos.contains(vm.getIdContrato()));
        contenidoExpandido.setManaged(expandidos.contains(vm.getIdContrato()));
        vm.getListaFacturas().forEach(f -> contenidoExpandido.getChildren().add(crearBloqueFactura(f, vm.getIdContrato())));

        HBox header = crearHeaderTarjeta(vm, chevron);
        header.setOnMouseClicked(e -> {
            boolean nuevoEstado = !contenidoExpandido.isVisible();
            contenidoExpandido.setVisible(nuevoEstado);
            contenidoExpandido.setManaged(nuevoEstado);
            chevron.setText(nuevoEstado ? "▼" : "▶");
            if (nuevoEstado) expandidos.add(vm.getIdContrato());
            else expandidos.remove(vm.getIdContrato());
        });

        tarjeta.getChildren().addAll(header, contenidoExpandido);
        return tarjeta;
    }

    private HBox crearHeaderTarjeta(ServicioContratadoViewModel vm, Label chevron) {
        Label icono = new Label(vm.getIcono());
        icono.setStyle("-fx-font-size: 16;");

        Label titulo = new Label(vm.getEmpresa());
        titulo.setStyle("-fx-text-fill: #e8c96d; -fx-font-weight: bold; -fx-font-size: 16;");

        Label subtitulo = new Label(vm.getNombreServicio());
        subtitulo.setStyle("-fx-text-fill: #666666; -fx-font-size: 13;");

        VBox textos = new VBox(2, titulo, subtitulo);

        Label badge = new Label(vm.isConMora() ? "● Con mora" : "● Al día");
        badge.setStyle(vm.isConMora()
                ? "-fx-text-fill: #e74c3c; -fx-background-color: #2a1010; -fx-padding: 4 10 4 10; -fx-background-radius: 12; -fx-font-size: 12;"
                : "-fx-text-fill: #27ae60; -fx-background-color: #0f2a18; -fx-padding: 4 10 4 10; -fx-background-radius: 12; -fx-font-size: 12;");

        HBox espaciador = new HBox();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        HBox header = new HBox(12, icono, textos, espaciador, badge, chevron);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-padding: 15; -fx-cursor: hand;");
        return header;
    }

    // ---- Bloque de una factura pendiente dentro de la tarjeta expandida ----
    private VBox crearBloqueFactura(FacturaPendienteViewModel factura, int idContrato) {
        VBox bloque = new VBox(6);
        bloque.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #262626; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 12;");

        Label periodo = new Label(factura.getPeriodoTexto());
        periodo.setStyle("-fx-text-fill: #e0e0e0; -fx-font-weight: bold;");
        Label vence = new Label("Vence: " + factura.getFechaVencimientoTexto());
        vence.setStyle("-fx-text-fill: #888888;");
        HBox filaPeriodo = filaConEspaciador(periodo, vence);

        Label lblCorteTitulo = new Label("Corte servicio");
        lblCorteTitulo.setStyle("-fx-text-fill: #888888;");
        Label corte = new Label("Corte: " + factura.getFechaCorteTexto());
        corte.setStyle("-fx-text-fill: #e74c3c;");
        HBox filaCorte = filaConEspaciador(lblCorteTitulo, corte);

        Label lblMontoTitulo = new Label("Monto original");
        lblMontoTitulo.setStyle("-fx-text-fill: #888888;");
        Label monto = new Label(String.format("$ %.2f", factura.getValorTotal()));
        monto.setStyle("-fx-text-fill: #e0e0e0;");
        HBox filaMonto = filaConEspaciador(lblMontoTitulo, monto);

        bloque.getChildren().addAll(filaPeriodo, filaCorte, filaMonto);

        if (factura.getInteresAcumulado() > 0) {
            Label lblMoraTitulo = new Label("Recargo mora");
            lblMoraTitulo.setStyle("-fx-text-fill: #888888;");
            Label mora = new Label(String.format("+ $ %.2f", factura.getInteresAcumulado()));
            mora.setStyle("-fx-text-fill: #e74c3c;");
            bloque.getChildren().add(filaConEspaciador(lblMoraTitulo, mora));
        }

        // Campo vacío (sin prellenar) + botón, según lo pedido
        TextField txtMonto = new TextField();
        txtMonto.setPromptText("0.00");
        txtMonto.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #cccccc; -fx-border-color: #2a2a2a; -fx-border-radius: 5; -fx-background-radius: 5;");
        HBox.setHgrow(txtMonto, Priority.ALWAYS);

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11;");
        lblError.setVisible(false);
        lblError.setManaged(false);

        Button btnAgregar = new Button("+ Añadir");
        btnAgregar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: #ffffff; -fx-cursor: hand;");
        btnAgregar.setOnAction(e -> onAgregarAlCarrito(factura, txtMonto, lblError));

        HBox filaAccion = new HBox(10, txtMonto, btnAgregar);

        bloque.getChildren().addAll(filaAccion, lblError);
        return bloque;
    }

    private HBox filaConEspaciador(Label izquierda, Label derecha) {
        HBox espaciador = new HBox();
        HBox.setHgrow(espaciador, Priority.ALWAYS);
        HBox fila = new HBox(izquierda, espaciador, derecha);
        fila.setAlignment(Pos.CENTER_LEFT);
        return fila;
    }

    private void onAgregarAlCarrito(FacturaPendienteViewModel factura, TextField txtMonto, Label lblError) {
        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            mostrarErrorFila(lblError, "Ingresa un monto numérico válido");
            return;
        }

        monto = BigDecimal.valueOf(monto).setScale(2, RoundingMode.HALF_UP).doubleValue();

        if (monto <= 0) {
            mostrarErrorFila(lblError, "El monto debe ser mayor a $0.00");
            return;
        }
        if (monto > factura.getValorTotal()) {
            mostrarErrorFila(lblError, "No puedes pagar más de lo que debes ($ " + String.format("%.2f", factura.getValorTotal()) + ")");
            return;
        }

        ocultarErrorFila(lblError);

        String cedula = SesionCliente.getCedulaActual();
        panelCliente.agregarAbonoAlCarrito(new AgregarAbonoDTOEntrada(cedula, factura.getIdFactura(), monto));
        cargarServicios(); // recarga todo; los campos vuelven a nacer vacíos
    }

    private void mostrarErrorFila(Label lblError, String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    private void ocultarErrorFila(Label lblError) {
        lblError.setVisible(false);
        lblError.setManaged(false);
    }

    @FXML private void onCarrito(ActionEvent e) { Navegador.irA("views/cliente/carrito.fxml"); e.consume(); }
    @FXML private void onPerfil(ActionEvent e) { Navegador.irA("views/cliente/perfilCliente.fxml"); e.consume(); }
    @FXML private void onSalir(ActionEvent e) { SesionCliente.cerrar(); Navegador.irA("views/cliente/loginCliente.fxml"); e.consume(); }
}