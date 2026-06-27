package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import servicart.data.FactoryDAO;
import servicart.domain.models.entities.*;
import servicart.domain.models.enums.EstadoFactura;
import servicart.domain.models.enums.ModalidadPago;
import servicart.domain.models.catalog.ServicioCatalogo;
import servicart.domain.services.ContratoService;
import servicart.domain.services.CorteService;
import servicart.domain.services.FacturacionService;
import servicart.dtos.ClienteDTO;
import servicart.dtos.FacturaDTO;
import servicart.dtos.FacturaMapper;
import servicart.ui.AppContext;
import servicart.ui.Navegador;
import servicart.ui.Sesion;
import java.time.LocalDateTime;
import java.util.List;

public class PanelClienteController {
    @FXML private VBox contenedorServicios;
    @FXML private Label lblMensaje;

    private ContratoService contratoService;
    private FacturacionService facturacionService;

    @FXML
    public void initialize() {
        contratoService = new ContratoService(FactoryDAO.contratoDAO());
        facturacionService = AppContext.getFacturacionService();
        cargarTarjetas();
    }

    private void cargarTarjetas() {
        contenedorServicios.getChildren().clear();
        ClienteDTO clienteDTO = Sesion.getClienteDTO();
        List<Contrato> contratos = contratoService.buscarPorCliente(clienteDTO.cedula());

        if (contratos.isEmpty()) {
            lblMensaje.setText("No tienes servicios contratados");
            lblMensaje.setVisible(true);

            return;
        }

        for (Contrato contrato : contratos) {
            List<Factura> facturas = facturacionService.buscarPorContrato(contrato.getId());
            contenedorServicios.getChildren().add(crearTarjeta(contrato, facturas));
        }
    }

    private VBox crearTarjeta(Contrato contrato, List<Factura> facturas) {
        ServicioCatalogo servicio = contrato.getServicio();

        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.getStyleClass().add("tarjeta-servicio");

        Label lblEmpresa = new Label(servicio.getEmpresa().name());
        lblEmpresa.getStyleClass().add("tarjeta-empresa");

        Label lblTipo = new Label(servicio.getTipo().name() + " · " + servicio.getTipoValor().name());
        lblTipo.getStyleClass().add("tarjeta-tipo");

        card.getChildren().addAll(lblEmpresa, lblTipo);

        // Panel de corte (si aplica)
        boolean tieneCorte = facturas.stream().anyMatch(Factura::superaFechaCorte);
        if (tieneCorte) card.getChildren().add(crearPanelCorte(contrato, servicio));

        // Facturas pendientes
        List<Factura> pendientes = facturas.stream().filter(f -> f.getEstado() != EstadoFactura.PAGADA).toList();

        if (pendientes.isEmpty()) card.getChildren().add(new Label("Sin facturas pendientes"));
        else for (Factura f : pendientes) card.getChildren().add(crearFilaFactura(f));

        return card;
    }

    private HBox crearPanelCorte(Contrato contrato, ServicioCatalogo servicio) {
        HBox panel = new HBox(10);
        panel.getStyleClass().add("panel-cortado");

        Label lbl = new Label("Servicio cortado — Costo reactivación: $" + servicio.getCostoReactivacion());

        Button btnReactivar = new Button("Reactivar");
        btnReactivar.setOnAction(e -> onReactivar(contrato, servicio.getCostoReactivacion()));

        panel.getChildren().addAll(lbl, btnReactivar);

        return panel;
    }

    private VBox crearFilaFactura(Factura factura) {
        VBox fila = new VBox(6);
        fila.getStyleClass().add("fila-factura");

        FacturaDTO dto = FacturaMapper.toDTO(factura);

        Label lblPeriodo = new Label("Período: " + dto.periodo());
        Label lblVencimiento = new Label("Vence: " + dto.fechaVencimiento());
        Label lblCorte = new Label("Corte: " + dto.fechaCorte());
        Label lblMonto = new Label("Monto: $" + dto.montoOriginal());

        fila.getChildren().addAll(lblPeriodo, lblVencimiento, lblCorte, lblMonto);

        // Mora
        if (factura.estaVencida() && dto.montoMora() > 0) {
            Label lblMora = new Label("Mora: $" + dto.montoMora());
            lblMora.getStyleClass().add("label-mora");
            fila.getChildren().add(lblMora);
        }

        // Control para añadir abono al carrito
        HBox hboxAbono = new HBox(8);
        TextField txtMonto = new TextField();
        txtMonto.setPromptText("Monto a abonar");
        txtMonto.setPrefWidth(140);

        ComboBox<ModalidadPago> cmbModalidad = new ComboBox<>();
        cmbModalidad.getItems().setAll(ModalidadPago.values());
        cmbModalidad.setValue(ModalidadPago.TC);

        Button btnAnadir = new Button("Añadir al carrito");
        btnAnadir.setOnAction(e -> onAnadirAbono(factura, txtMonto, cmbModalidad));

        hboxAbono.getChildren().addAll(txtMonto, cmbModalidad, btnAnadir);
        fila.getChildren().add(hboxAbono);

        return fila;
    }

    private void onAnadirAbono(Factura factura, TextField txtMonto, ComboBox<ModalidadPago> cmbModalidad) {
        try {
            double monto = Double.parseDouble(txtMonto.getText().trim());
            if (monto <= 0) throw new NumberFormatException();

            Abono abono = new Abono(monto, LocalDateTime.now(), false, factura, cmbModalidad.getValue());

            Sesion.getCarrito().agregarAbono(abono);
            mostrarMensaje("Abono de $" + monto + " añadido al carrito.");
            txtMonto.clear();
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese un monto válido mayor a 0");
        }
    }

    private void onReactivar(Contrato contrato, double costoReactivacion) {
        //Buscar el corte activo para este contrato
        CorteService corteService = new CorteService(FactoryDAO.corteServicioDAO());

        corteService.corteDAO.findAll().stream().filter(c -> c.getContrato().getId() == contrato.getId() && c.estadoCortado()).findFirst()
                .ifPresentOrElse(corte -> {
                    corteService.reactivarServicio(corte, costoReactivacion);
                    mostrarMensaje("Servicio reactivado. Costo: $" + costoReactivacion);
                    cargarTarjetas(); }, () -> mostrarMensaje("No se encontró un corte activo para este servicio"));
    }

    @FXML
    private void onMisServicios(ActionEvent event) { cargarTarjetas(); }

    @FXML
    private void onCarrito(ActionEvent event) { Navegador.irA("views/cliente/carrito.fxml"); }

    @FXML
    private void onPerfil(ActionEvent event) { Navegador.irA("views/cliente/perfilCliente.fxml"); }

    @FXML
    private void onSalir(ActionEvent event) {
        Sesion.cerrar();
        Navegador.irA("views/cliente/loginCliente.fxml");
    }

    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setVisible(true);
    }
}