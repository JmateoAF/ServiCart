package servicart.ui.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import servicart.data.FactoryDAO;
import servicart.domain.dtos.entradas.ForzarCorteDTOEntrada;
import servicart.domain.dtos.entradas.PagarFacturaDTOEntrada;
import servicart.domain.dtos.entradas.ReactivarCorteDTOEntrada;
import servicart.domain.dtos.retornos.CorteDTORetorno;
import servicart.domain.dtos.retornos.FacturaEnMoraDTORetorno;
import servicart.domain.dtos.retornos.ResumenCortesDTORetorno;
import servicart.domain.interfaces.AdminCortes;
import servicart.domain.services.BdService;
import servicart.entities.enums.ModalidadPago;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.AdminCortesMapperUI;
import servicart.ui.viewmodels.admin.CorteDetalleViewModel;
import servicart.ui.viewmodels.admin.CorteResumenViewModel;

import java.util.List;
import java.util.Optional;

public class AdminCortesController {
    @FXML private Button btnSQLite;
    @FXML private Button btnBinario;
    @FXML private Label lblServiciosCortados;
    @FXML private Label lblEnMora;
    @FXML private Label lblInteresesGenerados;

    @FXML private Button btnVerMora;
    @FXML private Button btnVerCortados;
    @FXML private VBox contenedorTarjetas;

    private final AdminCortes adminCortes;
    private boolean mostrandoCortados = false;

    public AdminCortesController(AdminCortes adminCortes) { this.adminCortes = adminCortes; }

    @FXML
    public void initialize() {
        cargarTodo();
        actualizarEstiloToggleBd(FactoryDAO.obtenerModoActual());
    }

    @FXML
    private void onSeleccionarSQLite(ActionEvent event) {
        cambiarModoBd("SQLite");
        event.consume();
    }

    @FXML
    private void onSeleccionarBinario(ActionEvent event) {
        cambiarModoBd("Binario");
        event.consume();
    }

    private void cambiarModoBd(String modo) {
        BdService.configurarBaseDatos(modo);
        cargarTodo();
        actualizarEstiloToggleBd(modo);
    }

    private void actualizarEstiloToggleBd(String modoActivo) {
        boolean sqlite = "SQLite".equalsIgnoreCase(modoActivo);
        btnSQLite.setStyle(estiloToggleBd(sqlite) + " -fx-background-radius: 5 0 0 5;");
        btnBinario.setStyle(estiloToggleBd(!sqlite) + " -fx-background-radius: 0 5 5 0;");
    }

    private String estiloToggleBd(boolean activo) {
        return activo
                ? "-fx-background-color: #1e1c0f; -fx-text-fill: #e8c96d; -fx-border-color: transparent; -fx-font-size: 15; -fx-padding: 5 15 5 15; -fx-cursor: hand;"
                : "-fx-background-color: #161616; -fx-text-fill: #555555; -fx-border-color: transparent; -fx-font-size: 15; -fx-padding: 5 15 5 15; -fx-cursor: hand;";
    }

    private void cargarTodo() {
        ResumenCortesDTORetorno resumenDto = adminCortes.obtenerResumen();
        CorteResumenViewModel resumen = AdminCortesMapperUI.resumenAViewModel(resumenDto);
        lblServiciosCortados.setText(String.valueOf(resumen.getTotalCortados()));
        lblEnMora.setText(String.valueOf(resumen.getTotalEnMora()));
        lblInteresesGenerados.setText(resumen.getTotalInteresesGenerados());

        List<FacturaEnMoraDTORetorno> enMoraDtos = adminCortes.listarEnMoraSinCorte();
        List<CorteDTORetorno> cortadosDtos = adminCortes.listarCortados();

        List<CorteDetalleViewModel> tarjetasEnMora = enMoraDtos.stream().map(AdminCortesMapperUI::enMoraADetalle).toList();
        List<CorteDetalleViewModel> tarjetasCortados = cortadosDtos.stream().map(AdminCortesMapperUI::corteADetalle).toList();

        actualizarToggle();
        renderizarTarjetas(mostrandoCortados ? tarjetasCortados : tarjetasEnMora);
    }

    private void renderizarTarjetas(List<CorteDetalleViewModel> filas) {
        contenedorTarjetas.getChildren().clear();

        if (filas.isEmpty()) {
            Label vacio = new Label(mostrandoCortados ? "No hay servicios cortados" : "No hay facturas en mora sin corte");
            vacio.setStyle("-fx-text-fill: #444444; -fx-font-size: 15;");
            contenedorTarjetas.getChildren().add(vacio);
            return;
        }

        filas.forEach(vm -> contenedorTarjetas.getChildren().add(crearTarjeta(vm)));
    }

    private VBox crearTarjeta(CorteDetalleViewModel vm) {
        VBox tarjeta = new VBox(10);
        String colorBorde = vm.isCortado() ? "#3a1a1a" : "#222222";
        tarjeta.setStyle("-fx-background-color: #161616; -fx-border-color: " + colorBorde + "; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");

        Label titulo = new Label("👤 " + vm.getNombreCliente() + " — " + vm.getServicio());
        titulo.setStyle("-fx-text-fill: #e8c96d; -fx-font-size: 15; -fx-font-family: 'Courier New';");

        Label estado = new Label(vm.isCortado()
                ? "🔴 " + vm.getDiasMora() + " días — CORTADO"
                : "⚠ " + vm.getDiasMora() + " días de mora");
        estado.setStyle(vm.isCortado()
                ? "-fx-text-fill: #c0392b; -fx-background-color: #2a1010; -fx-font-size: 15; -fx-padding: 0 5 0 5; -fx-background-radius: 4;"
                : "-fx-text-fill: #e07b39; -fx-background-color: #2a1a10; -fx-font-size: 15; -fx-padding: 0 5 0 5; -fx-background-radius: 4;");

        HBox header = new HBox(titulo, espaciador(), estado);
        header.setAlignment(Pos.CENTER_LEFT);

        HBox filaDeuda = filaInfo("Deuda original", vm.getDeudaOriginal(), "#aaaaaa");
        HBox filaInteres = filaInfo("Interés acumulado", vm.getInteresAcumulado(), vm.isCortado() ? "#c0392b" : "#e07b39");

        tarjeta.getChildren().addAll(header, filaDeuda, filaInteres);

        if (vm.isCortado()) {
            tarjeta.getChildren().add(filaInfo("Costo reactivación", vm.getCostoReactivacion(), "#aaaaaa"));
        }

        Label lblPie = new Label(vm.getPieTexto());
        lblPie.setStyle("-fx-text-fill: #444444; -fx-font-size: 15;");
        Label lblTotal = new Label((vm.isCortado() ? "Total con reactivación: $" : "Total: $") + vm.getTotal());
        lblTotal.setStyle("-fx-text-fill: #444444; -fx-font-size: 15;");
        tarjeta.getChildren().add(new HBox(lblPie, espaciador(), lblTotal));

        HBox acciones = new HBox(5);
        VBox.setMargin(acciones, new Insets(5, 0, 0, 0));

        Button btnPagar = new Button("Pagar");
        btnPagar.setStyle("-fx-background-color: #1c2a1c; -fx-text-fill: #4caf50; -fx-border-color: #2a3e2a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 15; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
        btnPagar.setOnAction(event -> { onPagar(vm.getIdFactura(), vm.getNombreCliente(), vm.getSaldoPendiente()); event.consume(); });
        acciones.getChildren().add(btnPagar);

        if (vm.isCortado()) {
            Button btnReactivar = new Button("Reactivar servicio");
            btnReactivar.setStyle("-fx-background-color: #0f2a18; -fx-text-fill: #27ae60; -fx-border-color: #1a3e28; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 15; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
            btnReactivar.setOnAction(event -> { onReactivar(vm.getIdCorte(), vm.getNombreCliente(), parseCosto(vm.getCostoReactivacion())); event.consume(); });
            acciones.getChildren().add(btnReactivar);
        } else {
            Button btnForzar = new Button("Forzar corte");
            btnForzar.setStyle("-fx-background-color: #2a1010; -fx-text-fill: #c0392b; -fx-border-color: #3a1a1a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 15; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
            btnForzar.setOnAction(event -> { onForzarCorte(vm.getIdContrato(), vm.getNombreCliente()); event.consume(); });
            acciones.getChildren().add(btnForzar);
        }

        tarjeta.getChildren().add(acciones);
        return tarjeta;
    }

    private HBox filaInfo(String etiqueta, String valor, String colorValor) {
        Label lblEtiqueta = new Label(etiqueta);
        lblEtiqueta.setStyle("-fx-text-fill: #555555; -fx-font-size: 15;");
        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-text-fill: " + colorValor + "; -fx-font-family: 'Courier New'; -fx-font-size: 15;");
        return new HBox(lblEtiqueta, espaciador(), lblValor);
    }

    private Region espaciador() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    private double parseCosto(String costoReactivacionTexto) {
        return costoReactivacionTexto == null ? 0 : Double.parseDouble(costoReactivacionTexto.replace(",", "."));
    }

    // Pago rápido desde el panel admin: registra un abono ya resuelto (pagoRealizado=true)
    // directamente sobre la factura, sin pasar por el carrito del cliente. Se asume transferencia
    // porque es un pago que el admin está reconciliando manualmente, no una modalidad elegida por el cliente
    private void onPagar(int idFactura, String nombreCliente, double saldoSugerido) {
        TextInputDialog dialogo = new TextInputDialog();
        Navegador.estilizarDialogo(dialogo);
        dialogo.setTitle("Registrar pago");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Monto a pagar de " + nombreCliente + " (máximo $" + String.format("%.2f", saldoSugerido) + "):");

        Optional<String> respuesta = dialogo.showAndWait();
        if (respuesta.isEmpty()) return;

        double monto;
        try {
            monto = Double.parseDouble(respuesta.get().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            mostrarError("Ingresa un monto numérico válido");
            return;
        }

        try {
            adminCortes.pagarFactura(new PagarFacturaDTOEntrada(idFactura, monto, ModalidadPago.TRANSFERENCIA));
            cargarTodo();
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void onForzarCorte(int idContrato, String nombreCliente) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        Navegador.estilizarDialogo(confirmacion);
        confirmacion.setTitle("Forzar corte");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Forzar el corte del servicio de " + nombreCliente + "? Esta acción no se puede deshacer");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) return;

        try {
            adminCortes.forzarCorte(new ForzarCorteDTOEntrada(idContrato));
            cargarTodo();
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void onReactivar(int idCorte, String nombreCliente, double costoRequerido) {
        TextInputDialog dialogo = new TextInputDialog();
        Navegador.estilizarDialogo(dialogo);
        dialogo.setTitle("Reactivar servicio");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Monto pagado por reactivación de " + nombreCliente + " (mínimo $" + String.format("%.2f", costoRequerido) + "):");

        Optional<String> respuesta = dialogo.showAndWait();
        if (respuesta.isEmpty()) return;

        double monto;
        try {
            monto = Double.parseDouble(respuesta.get().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            mostrarError("Ingresa un monto numérico válido");
            return;
        }

        if (monto < costoRequerido) {
            mostrarError("El monto debe ser al menos $" + String.format("%.2f", costoRequerido));
            return;
        }

        try {
            adminCortes.reactivar(new ReactivarCorteDTOEntrada(idCorte, monto));
            cargarTodo();
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        Navegador.estilizarDialogo(alerta);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void onVerMora(ActionEvent event) {
        mostrandoCortados = false;
        cargarTodo();
        event.consume();
    }

    @FXML
    private void onVerCortados(ActionEvent event) {
        mostrandoCortados = true;
        cargarTodo();
        event.consume();
    }

    private void actualizarToggle() {
        btnVerMora.setStyle((mostrandoCortados ? estiloToggleInactivo() : estiloToggleActivo()) + "-fx-background-radius: 5 0 0 5;");
        btnVerCortados.setStyle((mostrandoCortados ? estiloToggleActivo() : estiloToggleInactivo()) + "-fx-background-radius: 0 5 5 0;");
    }

    private String estiloToggleActivo() { return "-fx-background-color: #1e1c0f; -fx-text-fill: #e8c96d; -fx-border-color: transparent; -fx-font-size: 15; -fx-padding: 5 15 5 15; -fx-cursor: hand; "; }
    private String estiloToggleInactivo() { return "-fx-background-color: #161616; -fx-text-fill: #555555; -fx-border-color: transparent; -fx-font-size: 15; -fx-padding: 5 15 5 15; -fx-cursor: hand; "; }

    @FXML private void onUsuarios(ActionEvent event) { Navegador.irA("views/admin/adminUsuarios.fxml"); event.consume(); }
    @FXML private void onTarifas(ActionEvent event) { Navegador.irA("views/admin/adminTarifas.fxml"); event.consume(); }
    @FXML private void onCortes(ActionEvent event) { Navegador.irA("views/admin/adminCortes.fxml"); event.consume(); }
    @FXML private void onEmpresas(ActionEvent event) { Navegador.irA("views/admin/adminEmpresas.fxml"); event.consume(); }
    @FXML private void onSalir(ActionEvent event) { Navegador.irA("views/admin/loginAdmin.fxml"); event.consume(); }
}
