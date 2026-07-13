package servicart.ui.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import servicart.domain.dtos.entradas.ForzarCorteDTOEntrada;
import servicart.domain.dtos.entradas.ReactivarCorteDTOEntrada;
import servicart.domain.dtos.retornos.CorteDTORetorno;
import servicart.domain.dtos.retornos.FacturaEnMoraDTORetorno;
import servicart.domain.dtos.retornos.ResumenCortesDTORetorno;
import servicart.domain.interfaces.AdminCortes;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.AdminCortesMapperUI;
import servicart.ui.viewmodels.admin.CorteDetalleViewModel;
import servicart.ui.viewmodels.admin.CorteHistorialViewModel;
import servicart.ui.viewmodels.admin.CorteResumenViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminCortesController {
    @FXML private Label lblServiciosCortados;
    @FXML private Label lblEnMora;
    @FXML private Label lblInteresesGenerados;

    @FXML private Button btnVerMora;
    @FXML private Button btnVerCortados;
    @FXML private VBox contenedorTarjetas;

    @FXML private TableView<CorteHistorialViewModel> tblHistorial;
    @FXML private TableColumn<CorteHistorialViewModel, String> colUsuario;
    @FXML private TableColumn<CorteHistorialViewModel, String> colServicio;
    @FXML private TableColumn<CorteHistorialViewModel, String> colDiasMora;
    @FXML private TableColumn<CorteHistorialViewModel, String> colInteres;
    @FXML private TableColumn<CorteHistorialViewModel, String> colEstado;
    @FXML private TableColumn<CorteHistorialViewModel, Void> colAcciones;

    private final AdminCortes adminCortes;
    private boolean mostrandoCortados = false;

    public AdminCortesController(AdminCortes adminCortes) { this.adminCortes = adminCortes; }

    @FXML
    public void initialize() {
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
        colDiasMora.setCellValueFactory(new PropertyValueFactory<>("diasMora"));
        colInteres.setCellValueFactory(new PropertyValueFactory<>("interes"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colAcciones.setCellFactory(columna -> crearCeldaAcciones());

        cargarTodo();
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

        List<CorteHistorialViewModel> historial = new ArrayList<>();
        historial.addAll(enMoraDtos.stream().map(AdminCortesMapperUI::enMoraAHistorial).toList());
        historial.addAll(cortadosDtos.stream().map(AdminCortesMapperUI::corteAHistorial).toList());
        tblHistorial.getItems().setAll(historial);
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

        tarjeta.getChildren().add(crearBarraProgreso(vm));

        Label lblPie = new Label(vm.getPieTexto());
        lblPie.setStyle("-fx-text-fill: #444444; -fx-font-size: 15;");
        Label lblTotal = new Label((vm.isCortado() ? "Total con reactivación: $" : "Total: $") + vm.getTotal());
        lblTotal.setStyle("-fx-text-fill: #444444; -fx-font-size: 15;");
        tarjeta.getChildren().add(new HBox(lblPie, espaciador(), lblTotal));

        HBox acciones = new HBox(5);
        VBox.setMargin(acciones, new Insets(5, 0, 0, 0));

        Button btnDetalle = new Button("Ver detalle");
        btnDetalle.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #888888; -fx-border-color: #2a2a2a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 15; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
        btnDetalle.setOnAction(event -> { mostrarDetalle(vm); event.consume(); });
        acciones.getChildren().add(btnDetalle);

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

    private StackPane crearBarraProgreso(CorteDetalleViewModel vm) {
        StackPane fondo = new StackPane();
        fondo.setPrefHeight(4);
        fondo.setStyle("-fx-background-color: #1e1e1e; -fx-background-radius: 2;");

        HBox contenedor = new HBox();
        contenedor.setAlignment(Pos.CENTER_LEFT);

        Region relleno = new Region();
        relleno.setPrefHeight(4);

        if (vm.isCortado()) {
            relleno.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(relleno, Priority.ALWAYS);
            relleno.setStyle("-fx-background-color: #c0392b; -fx-background-radius: 2;");
        } else {
            relleno.setPrefWidth(Math.max(4, vm.getProgresoCorte() * 220));
            relleno.setStyle("-fx-background-color: #e07b39; -fx-background-radius: 2;");
        }

        contenedor.getChildren().add(relleno);
        fondo.getChildren().add(contenedor);
        return fondo;
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
        return costoReactivacionTexto == null ? 0 : Double.parseDouble(costoReactivacionTexto);
    }

    private TableCell<CorteHistorialViewModel, Void> crearCeldaAcciones() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }

                CorteHistorialViewModel vm = getTableView().getItems().get(getIndex());
                Button boton = new Button(vm.isCortado() ? "Reactivar" : "Forzar corte");
                boton.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #e8c96d; -fx-border-color: #2e2e2e; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 15; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
                boton.setOnAction(event -> {
                    if (vm.isCortado()) onReactivar(vm.getIdCorte(), vm.getUsuario(), buscarCostoReactivacion(vm.getIdCorte()));
                    else onForzarCorte(vm.getIdContrato(), vm.getUsuario());
                    event.consume();
                });
                setGraphic(boton);
            }
        };
    }

    // El historial no trae el costo de reactivación crudo (solo texto de tarjeta); se recalcula
    // desde la lista de cortados para no tener que ampliar CorteHistorialViewModel con más campos
    private double buscarCostoReactivacion(int idCorte) {
        return adminCortes.listarCortados().stream()
                .filter(c -> c.idCorte() == idCorte)
                .map(CorteDTORetorno::costoReactivacion)
                .findFirst()
                .orElse(0.0);
    }

    private void mostrarDetalle(CorteDetalleViewModel vm) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Detalle");
        alerta.setHeaderText(vm.getNombreCliente() + " — " + vm.getServicio());
        alerta.setContentText(
                "Deuda original: $" + vm.getDeudaOriginal() + "\n"
                + "Interés acumulado: " + vm.getInteresAcumulado() + "\n"
                + (vm.isCortado()
                    ? "Días cortado: " + vm.getDiasMora() + "\nCosto reactivación: $" + vm.getCostoReactivacion()
                    : "Días de mora: " + vm.getDiasMora() + "\n" + vm.getPieTexto()));
        alerta.showAndWait();
    }

    private void onForzarCorte(int idContrato, String nombreCliente) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
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
        TextInputDialog dialogo = new TextInputDialog(String.format("%.2f", costoRequerido));
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

    @FXML private void onDashboard(ActionEvent event) { Navegador.irA("views/admin/adminDashboard.fxml"); event.consume(); }
    @FXML private void onUsuarios(ActionEvent event) { Navegador.irA("views/admin/adminUsuarios.fxml"); event.consume(); }
    @FXML private void onTarifas(ActionEvent event) { Navegador.irA("views/admin/adminTarifas.fxml"); event.consume(); }
    @FXML private void onCortes(ActionEvent event) { Navegador.irA("views/admin/adminCortes.fxml"); event.consume(); }
    @FXML private void onSalir(ActionEvent event) { Navegador.irA("views/admin/loginAdmin.fxml"); event.consume(); }
}
