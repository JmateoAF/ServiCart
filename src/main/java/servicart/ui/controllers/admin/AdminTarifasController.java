package servicart.ui.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import servicart.domain.dtos.entradas.ActualizarTarifaDTOEntrada;
import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;
import servicart.domain.interfaces.AdminTarifas;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.AdminTarifasMapperUI;
import servicart.ui.viewmodels.admin.ServicioCatalogoViewModel;

import java.util.List;

public class AdminTarifasController {
    @FXML private GridPane gridTarifas;
    @FXML private Label lblFormularioTitulo;
    @FXML private TextField txtTarifaBase;
    @FXML private TextField txtInteresMora;
    @FXML private TextField txtDiasCorte;
    @FXML private TextField txtCostoReactivacion;

    private final AdminTarifas adminTarifas;
    private int idSeleccionado = -1;

    public AdminTarifasController(AdminTarifas adminTarifas) { this.adminTarifas = adminTarifas; }

    @FXML
    public void initialize() {
        limpiarFormulario();
        cargarTarifas();
    }

    private void cargarTarifas() {
        List<TarifaDetalleDTORetorno> dtos = adminTarifas.listarTarifas();
        List<ServicioCatalogoViewModel> vms = dtos.stream().map(AdminTarifasMapperUI::dtoAViewModel).toList();

        gridTarifas.getChildren().clear();

        for (int i = 0; i < vms.size(); i++) {
            VBox tarjeta = crearTarjeta(vms.get(i));
            gridTarifas.add(tarjeta, i % 2, i / 2);
        }
    }

    private VBox crearTarjeta(ServicioCatalogoViewModel vm) {
        VBox tarjeta = new VBox(10);
        tarjeta.setStyle("-fx-background-color: #161616; -fx-border-color: #222222; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 15;");

        Label titulo = new Label(vm.getNombreServicio() + " — " + vm.getEmpresaNombre());
        titulo.setStyle("-fx-text-fill: #e8c96d; -fx-font-size: 15; -fx-font-family: 'Courier New';");

        Label badge = new Label(vm.getTipoValor());
        badge.setStyle("-fx-text-fill: #555555; -fx-background-color: #1e1e1e; -fx-font-size: 15; -fx-padding: 0 5 0 5; -fx-background-radius: 4;");

        HBox header = new HBox(titulo, espaciador(), badge);
        header.setAlignment(Pos.CENTER_LEFT);

        HBox filaTarifa = filaInfo("Tarifa base", vm.getTarifa());
        HBox filaInteres = filaInfo("Interés mora diario", vm.getInteresMora());
        HBox filaDias = filaInfo("Días para corte", vm.getDiasParaCorte());
        HBox filaCosto = filaInfo("Costo reactivación", vm.getCostoReactivacion());

        Button btnEditar = new Button("Editar tarifa");
        btnEditar.setMaxWidth(Double.MAX_VALUE);
        btnEditar.setStyle("-fx-background-color: transparent; -fx-text-fill: #e8c96d; -fx-border-color: #e8c96d; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 15; -fx-padding: 5 0 5 0; -fx-cursor: hand;");
        VBox.setMargin(btnEditar, new Insets(5, 0, 0, 0));
        btnEditar.setOnAction(event -> { cargarEnFormulario(vm); event.consume(); });

        tarjeta.getChildren().addAll(header, filaTarifa, filaInteres, filaDias, filaCosto, btnEditar);
        return tarjeta;
    }

    private HBox filaInfo(String etiqueta, String valor) {
        Label lblEtiqueta = new Label(etiqueta);
        lblEtiqueta.setStyle("-fx-text-fill: #555555; -fx-font-size: 15;");
        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-text-fill: #aaaaaa; -fx-font-family: 'Courier New'; -fx-font-size: 15;");
        return new HBox(lblEtiqueta, espaciador(), lblValor);
    }

    private Region espaciador() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    private void cargarEnFormulario(ServicioCatalogoViewModel vm) {
        idSeleccionado = vm.getId();
        lblFormularioTitulo.setText("EDITAR TARIFA — " + vm.getNombreServicio());
        txtTarifaBase.setText(String.valueOf(vm.getTarifaBaseValor()));
        txtInteresMora.setText(String.valueOf(vm.getInteresMoraPorcentajeValor()));
        txtDiasCorte.setText(vm.getDiasParaCorte());
        txtCostoReactivacion.setText(String.valueOf(vm.getCostoReactivacionValor()));
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        if (idSeleccionado == -1) {
            mostrarError("Selecciona un servicio con \"Editar tarifa\" antes de guardar");
            event.consume();
            return;
        }

        double tarifaBase;
        double interesMora;
        double costoReactivacion;

        try {
            tarifaBase = Double.parseDouble(txtTarifaBase.getText().trim().replace(",", "."));
            interesMora = Double.parseDouble(txtInteresMora.getText().trim().replace(",", "."));
            costoReactivacion = Double.parseDouble(txtCostoReactivacion.getText().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            mostrarError("Ingresa valores numéricos válidos");
            event.consume();
            return;
        }

        if (tarifaBase < 0 || interesMora < 0 || costoReactivacion < 0) {
            mostrarError("Los valores no pueden ser negativos");
            event.consume();
            return;
        }

        adminTarifas.actualizarTarifa(new ActualizarTarifaDTOEntrada(idSeleccionado, tarifaBase, interesMora, costoReactivacion));

        limpiarFormulario();
        cargarTarifas();
        event.consume();
    }

    @FXML
    private void onCancelar(ActionEvent event) {
        limpiarFormulario();
        event.consume();
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        lblFormularioTitulo.setText("EDITAR TARIFA");
        txtTarifaBase.clear();
        txtInteresMora.clear();
        txtDiasCorte.clear();
        txtCostoReactivacion.clear();
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML private void onDashboard(ActionEvent event) { Navegador.irA("views/admin/adminDashboard.fxml"); event.consume(); }
    @FXML private void onUsuarios(ActionEvent event) { Navegador.irA("views/admin/adminUsuarios.fxml"); event.consume(); }
    @FXML private void onTarifas(ActionEvent event) { Navegador.irA("views/admin/adminTarifas.fxml"); event.consume(); }
    @FXML private void onCortes(ActionEvent event) { Navegador.irA("views/admin/adminCortes.fxml"); event.consume(); }
    @FXML private void onSalir(ActionEvent event) { Navegador.irA("views/admin/loginAdmin.fxml"); event.consume(); }
}
