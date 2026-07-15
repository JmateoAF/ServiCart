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
import servicart.domain.dtos.retornos.ServicioContratadoDTORetorno;
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
    private final PanelCliente panelCliente;
    private final Set<Integer> expandidos = new HashSet<>();
    @FXML
    private VBox contenedorServicios;
    @FXML
    private Label lblMensaje;

    public PanelClienteController(PanelCliente panelCliente) {
        this.panelCliente = panelCliente;
    }

    @FXML
    public void initialize() {
        cargarServicios();
    }

    private void cargarServicios() {
        String cedula = SesionCliente.getCedulaActual();
        List<ServicioContratadoDTORetorno> dtos = panelCliente.listarServiciosContratados(new PanelClienteDTOEntrada(cedula));
        List<ServicioContratadoViewModel> vms = dtos.stream().map(PanelClienteMapperUI::DTOAviewModel).toList();

        contenedorServicios.getChildren().clear();

        if (vms.isEmpty()) {
            lblMensaje.setText("No tienes servicios contratados");
            return;
        }
        lblMensaje.setText("");

        vms.forEach(vm -> contenedorServicios.getChildren().add(crearTarjetaServicio(vm)));
        contenedorServicios.getChildren().add(crearFilaTotal(vms));
    }

    private VBox crearTarjetaServicio(ServicioContratadoViewModel vm) {
        VBox tarjeta = new VBox();
        tarjeta.setStyle("-fx-background-color: #161616; -fx-border-color: #252525; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label lbl = new Label(expandidos.contains(vm.getIdContrato()) ? "▼" : "▶");
        lbl.setStyle("-fx-text-fill: #444444; -fx-font-size: 15;");

        VBox contenidoExpandido = new VBox(15);
        contenidoExpandido.setStyle("-fx-padding: 10;");
        contenidoExpandido.setVisible(expandidos.contains(vm.getIdContrato()));
        contenidoExpandido.setManaged(expandidos.contains(vm.getIdContrato()));

        if (vm.isEstaCortado()) contenidoExpandido.getChildren().add(crearAvisoCorte(vm));
        vm.getListaFacturas().forEach(f -> contenidoExpandido.getChildren().add(crearBloqueFactura(f)));
        contenidoExpandido.getChildren().add(crearSubtotalServicio(vm)); // NUEVO

        HBox header = crearHeaderTarjeta(vm, lbl);
        header.setOnMouseClicked(event -> {
            boolean nuevoEstado = !contenidoExpandido.isVisible();
            contenidoExpandido.setVisible(nuevoEstado);
            contenidoExpandido.setManaged(nuevoEstado);
            lbl.setText(nuevoEstado ? "▼" : "▶");
            if (nuevoEstado) expandidos.add(vm.getIdContrato());
            else expandidos.remove(vm.getIdContrato());
            event.consume();
        });

        tarjeta.getChildren().addAll(header, contenidoExpandido);
        return tarjeta;
    }

    private HBox crearSubtotalServicio(ServicioContratadoViewModel vm) {
        Label titulo = new Label("Subtotal " + vm.getEmpresa());
        titulo.setStyle("-fx-text-fill: #e8c96d; -fx-font-weight: bold; -fx-font-size: 15;");
        Label valor = new Label(vm.getSubtotalServicioTexto());
        valor.setStyle("-fx-text-fill: #e8c96d; -fx-font-weight: bold; -fx-font-size: 15;");
        HBox fila = filaConEspaciador(titulo, valor);
        fila.setStyle("-fx-border-color: #333333; -fx-border-width: 1 0 0 0; -fx-padding: 10 0 0 0;");
        return fila;
    }

    private HBox crearHeaderTarjeta(ServicioContratadoViewModel vm, Label lbl) {
        Label titulo = new Label(vm.getEmpresa());
        titulo.setStyle("-fx-text-fill: #e8c96d; -fx-font-weight: bold; -fx-font-size: 20;");

        Label subtitulo = new Label(vm.getNombreServicio());
        subtitulo.setStyle("-fx-text-fill: #555555; -fx-font-size: 15;");

        VBox textos = new VBox(0, titulo, subtitulo);

        Label badge = new Label(textoBadge(vm));
        badge.setStyle(estiloBadge(vm) + " -fx-padding: 5 10 5 10; -fx-background-radius: 10; -fx-font-size: 15;");

        HBox espaciador = new HBox();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        HBox header = new HBox(10, textos, espaciador, badge, lbl);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-padding: 15; -fx-cursor: hand;");
        return header;
    }

    private String textoBadge(ServicioContratadoViewModel vm) {
        if (vm.isEstaCortado()) return "Servicio cortado";
        if (vm.isConMora()) return "Con mora";
        return "Al día";
    }

    private String estiloBadge(ServicioContratadoViewModel vm) {
        if (vm.isEstaCortado()) return "-fx-text-fill: #e74c3c; -fx-background-color: #2a1010;";
        if (vm.isConMora()) return "-fx-text-fill: #e8b93d; -fx-background-color: #2a2410;";
        return "-fx-text-fill: #27ae60; -fx-background-color: #0f2a18;";
    }

    private VBox crearAvisoCorte(ServicioContratadoViewModel vm) {
        VBox aviso = new VBox(5);
        aviso.setStyle("-fx-background-color: #2a1010; -fx-border-color: #4a1c1c; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10;");

        Label titulo = new Label("Servicio cortado por falta de pago");
        titulo.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 15;");

        Label detalle = new Label("Costo de reactivación: " + vm.getCostoReactivacionTexto()
                + " (adicional a la deuda pendiente)");
        detalle.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 15;");

        aviso.getChildren().addAll(titulo, detalle);
        return aviso;
    }

    private VBox crearBloqueFactura(FacturaPendienteViewModel factura) {
        VBox bloque = new VBox(5);
        bloque.setStyle("-fx-background-color: #111111; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10;");

        Label periodo = new Label(factura.getPeriodoTexto());
        periodo.setStyle("-fx-text-fill: #888888; -fx-font-weight: bold; -fx-font-size: 15;");
        Label vence = new Label("Vence: " + factura.getFechaVencimientoTexto());
        vence.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 15;");
        HBox filaPeriodo = filaConEspaciador(periodo, vence);

        Label lblCorteTitulo = new Label("Corte servicio");
        lblCorteTitulo.setStyle("-fx-text-fill: #555555; -fx-font-size: 15;");
        Label corte = new Label("Corte: " + factura.getFechaCorteTexto());
        corte.setStyle("-fx-text-fill: #c0392b; -fx-font-size: 15;");
        HBox filaCorte = filaConEspaciador(lblCorteTitulo, corte);

        Label lblMontoTitulo = new Label("Monto original");
        lblMontoTitulo.setStyle("-fx-text-fill: #555555; -fx-font-size: 15;");
        Label monto = new Label(String.format("$ %.2f", factura.getValorBase()));
        monto.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 15;");
        HBox filaMonto = filaConEspaciador(lblMontoTitulo, monto);

        bloque.getChildren().addAll(filaPeriodo, filaCorte, filaMonto);

        if (factura.getInteresAcumulado() > 0) {
            Label lblMoraTitulo = new Label("Recargo mora");
            lblMoraTitulo.setStyle("-fx-text-fill: #555555; -fx-font-size: 15;");
            Label mora = new Label(String.format("+ $ %.2f", factura.getInteresAcumulado()));
            mora.setStyle("-fx-text-fill: #c0392b; -fx-font-size: 15;");
            bloque.getChildren().add(filaConEspaciador(lblMoraTitulo, mora));
        }

        Label lblTotalTitulo = new Label("Saldo pendiente");
        lblTotalTitulo.setStyle("-fx-text-fill: #888888; -fx-font-weight: bold; -fx-font-size: 15;");
        Label totalIndividual = new Label(factura.getTotalIndividualTexto());
        totalIndividual.setStyle("-fx-text-fill: #e8c96d; -fx-font-weight: bold; -fx-font-size: 15;");
        bloque.getChildren().add(filaConEspaciador(lblTotalTitulo, totalIndividual));

        TextField txtMonto = new TextField();
        txtMonto.setPromptText("0.00");
        txtMonto.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #cccccc; -fx-border-color: #2a2a2a; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 15;");
        HBox.setHgrow(txtMonto, Priority.ALWAYS);

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 15;");
        lblError.setVisible(false);
        lblError.setManaged(false);

        Button btnAgregar = new Button("Añadir");
        btnAgregar.setStyle("-fx-background-color: #1c2a1c; -fx-text-fill: #4caf50; -fx-cursor: hand; -fx-font-size: 15;");
        btnAgregar.setOnAction(event -> {
            onAgregarAlCarrito(factura, txtMonto, lblError);
            event.consume();
        });

        HBox filaAccion = new HBox(10, txtMonto, btnAgregar);
        bloque.getChildren().addAll(filaAccion, lblError);

        return bloque;
    }

    private HBox crearFilaTotal(List<ServicioContratadoViewModel> vms) {
        double total = vms.stream()
                .flatMap(vm -> vm.getListaFacturas().stream())
                .mapToDouble(FacturaPendienteViewModel::getSaldoPendiente).sum()
                + vms.stream().filter(ServicioContratadoViewModel::isEstaCortado)
                .mapToDouble(ServicioContratadoViewModel::getCostoReactivacion).sum();

        Label lblTitulo = new Label("Total a pagar");
        lblTitulo.setStyle("-fx-text-fill: #e8c96d; -fx-font-weight: bold; -fx-font-size: 15;");
        Label lblTotal = new Label(String.format("$ %.2f", total));
        lblTotal.setStyle("-fx-text-fill: #e8c96d; -fx-font-weight: bold; -fx-font-size: 15;");

        HBox espaciador = new HBox();
        HBox.setHgrow(espaciador, Priority.ALWAYS);
        HBox fila = new HBox(10, lblTitulo, espaciador, lblTotal);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setStyle("-fx-background-color: #161616; -fx-border-color: #e8c96d; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15;");
        VBox.setMargin(fila, new Insets(10, 0, 0, 0));

        return fila;
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
        if (monto > factura.getSaldoPendiente() + 0.005) {
            mostrarErrorFila(lblError, "No puedes pagar más de lo que debes ($ " + String.format("%.2f", factura.getSaldoPendiente()) + ")");
            return;
        }

        ocultarErrorFila(lblError);

        String cedula = SesionCliente.getCedulaActual();
        panelCliente.agregarAbonoAlCarrito(new AgregarAbonoDTOEntrada(cedula, factura.getIdFactura(), monto));
        cargarServicios();
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

    @FXML
    private void onCarrito(ActionEvent event) {
        Navegador.irA("views/cliente/carrito.fxml");
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
}