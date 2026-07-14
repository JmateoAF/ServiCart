package servicart.ui.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import servicart.domain.dtos.entradas.CrearEmpresaDTOEntrada;
import servicart.domain.dtos.entradas.CrearServicioDTOEntrada;
import servicart.domain.dtos.retornos.EmpresaDTORetorno;
import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;
import servicart.domain.interfaces.AdminEmpresas;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.AdminEmpresasMapperUI;
import servicart.ui.mappers.AdminTarifasMapperUI;
import servicart.ui.viewmodels.admin.EmpresaTablaViewModel;
import servicart.ui.viewmodels.admin.ServicioCatalogoViewModel;

import java.util.List;
import java.util.Optional;

public class AdminEmpresasController {
    @FXML private VBox listaEmpresas;
    @FXML private TextField txtNombreEmpresaForm;

    @FXML private VBox listaServicios;
    @FXML private ComboBox<String> cmbEmpresaForm;
    @FXML private ComboBox<String> cmbTipoServicioForm;
    @FXML private ComboBox<String> cmbTipoValorForm;
    @FXML private TextField txtTarifaBaseForm;
    @FXML private TextField txtInteresMoraForm;
    @FXML private TextField txtCostoReactivacionForm;
    @FXML private TextField txtDiasCorteForm;

    private final AdminEmpresas adminEmpresas;
    private List<EmpresaDTORetorno> empresasCache = List.of();

    public AdminEmpresasController(AdminEmpresas adminEmpresas) { this.adminEmpresas = adminEmpresas; }

    @FXML
    public void initialize() {
        cmbTipoServicioForm.getItems().setAll("AGUA", "LUZ", "BASURA", "INTERNET");
        cmbTipoValorForm.getItems().setAll("FIJO", "VARIABLE");

        cargarTodo();
    }

    private void cargarTodo() {
        empresasCache = adminEmpresas.listarEmpresas();
        cmbEmpresaForm.getItems().setAll(empresasCache.stream().map(EmpresaDTORetorno::nombre).toList());

        List<EmpresaTablaViewModel> filasEmpresas = empresasCache.stream().map(AdminEmpresasMapperUI::dtoAViewModel).toList();
        listaEmpresas.getChildren().setAll(filasEmpresas.stream().map(this::crearFilaEmpresa).toList());

        List<TarifaDetalleDTORetorno> serviciosDtos = adminEmpresas.listarServicios();
        List<ServicioCatalogoViewModel> filasServicios = serviciosDtos.stream().map(AdminTarifasMapperUI::dtoAViewModel).toList();
        listaServicios.getChildren().setAll(filasServicios.stream().map(this::crearFilaServicio).toList());
    }

    private HBox crearFilaEmpresa(EmpresaTablaViewModel vm) {
        Label nombre = new Label(vm.getNombre());
        nombre.setPrefWidth(250);
        nombre.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 15;");

        Label cantidad = new Label(vm.getCantidadServiciosTexto());
        cantidad.setStyle("-fx-text-fill: #888888; -fx-font-size: 15;");

        Region relleno = new Region();
        HBox.setHgrow(relleno, Priority.ALWAYS);

        HBox fila = new HBox(15, nombre, cantidad, relleno);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setStyle("-fx-padding: 10 15 10 15; -fx-border-color: #1e1e1e; -fx-border-width: 0 0 1 0;");
        return fila;
    }

    private HBox crearFilaServicio(ServicioCatalogoViewModel vm) {
        Label empresa = new Label(vm.getEmpresaNombre());
        empresa.setPrefWidth(150);
        empresa.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 15;");

        Label servicio = new Label(vm.getNombreServicio());
        servicio.setPrefWidth(150);
        servicio.setStyle("-fx-text-fill: #888888; -fx-font-size: 15;");

        Label tipoValor = new Label(vm.getTipoValor());
        tipoValor.setPrefWidth(90);
        tipoValor.setStyle("-fx-text-fill: #888888; -fx-font-size: 15;");

        Label tarifa = new Label(vm.getTarifa());
        tarifa.setPrefWidth(150);
        tarifa.setStyle("-fx-text-fill: #e8c96d; -fx-font-family: 'Courier New'; -fx-font-size: 15;");

        Button btnQuitar = new Button("Quitar");
        btnQuitar.setStyle("-fx-background-color: #2a1010; -fx-text-fill: #c0392b; -fx-border-color: #3a1a1a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 15; -fx-padding: 5 10 5 10; -fx-cursor: hand;");
        btnQuitar.setOnAction(event -> { onQuitarServicio(vm.getId(), vm.getEmpresaNombre() + " — " + vm.getNombreServicio()); event.consume(); });

        Region relleno = new Region();
        HBox.setHgrow(relleno, Priority.ALWAYS);

        HBox fila = new HBox(15, empresa, servicio, tipoValor, tarifa, btnQuitar, relleno);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setStyle("-fx-padding: 10 15 10 15; -fx-border-color: #1e1e1e; -fx-border-width: 0 0 1 0;");
        return fila;
    }

    @FXML
    private void onGuardarEmpresa(ActionEvent event) {
        try {
            adminEmpresas.crearEmpresa(new CrearEmpresaDTOEntrada(txtNombreEmpresaForm.getText()));
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
            event.consume();
            return;
        }

        txtNombreEmpresaForm.clear();
        cargarTodo();
        event.consume();
    }

    @FXML
    private void onGuardarServicio(ActionEvent event) {
        String nombreEmpresa = cmbEmpresaForm.getValue();
        String tipoServicio = cmbTipoServicioForm.getValue();
        String tipoValor = cmbTipoValorForm.getValue();

        if (nombreEmpresa == null || tipoServicio == null || tipoValor == null) {
            mostrarError("Selecciona empresa, tipo de servicio y tipo de tarifa");
            event.consume();
            return;
        }

        Optional<EmpresaDTORetorno> empresa = empresasCache.stream().filter(e -> e.nombre().equals(nombreEmpresa)).findFirst();
        if (empresa.isEmpty()) {
            mostrarError("Empresa no válida");
            event.consume();
            return;
        }

        double tarifaBase;
        double interesMora;
        double costoReactivacion;
        int diasParaCorte;
        try {
            tarifaBase = Double.parseDouble(txtTarifaBaseForm.getText().trim().replace(",", "."));
            interesMora = Double.parseDouble(txtInteresMoraForm.getText().trim().replace(",", "."));
            costoReactivacion = Double.parseDouble(txtCostoReactivacionForm.getText().trim().replace(",", "."));
            diasParaCorte = Integer.parseInt(txtDiasCorteForm.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarError("Ingresa valores numéricos válidos (días para corte debe ser un número entero)");
            event.consume();
            return;
        }

        try {
            adminEmpresas.crearServicio(new CrearServicioDTOEntrada(
                    empresa.get().id(), tipoServicio, tipoValor, tarifaBase, interesMora, costoReactivacion, diasParaCorte));
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
            event.consume();
            return;
        }

        limpiarFormularioServicio();
        cargarTodo();
        event.consume();
    }

    private void onQuitarServicio(int idServicio, String descripcion) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        Navegador.estilizarDialogo(confirmacion);
        confirmacion.setTitle("Quitar servicio");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Quitar " + descripcion + " del catálogo? Esta acción no se puede deshacer");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) return;

        try {
            adminEmpresas.eliminarServicio(idServicio);
            cargarTodo();
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void limpiarFormularioServicio() {
        cmbEmpresaForm.setValue(null);
        cmbTipoServicioForm.setValue(null);
        cmbTipoValorForm.setValue(null);
        txtTarifaBaseForm.clear();
        txtInteresMoraForm.clear();
        txtCostoReactivacionForm.clear();
        txtDiasCorteForm.clear();
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        Navegador.estilizarDialogo(alerta);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML private void onUsuarios(ActionEvent event) { Navegador.irA("views/admin/adminUsuarios.fxml"); event.consume(); }
    @FXML private void onTarifas(ActionEvent event) { Navegador.irA("views/admin/adminTarifas.fxml"); event.consume(); }
    @FXML private void onCortes(ActionEvent event) { Navegador.irA("views/admin/adminCortes.fxml"); event.consume(); }
    @FXML private void onEmpresas(ActionEvent event) { Navegador.irA("views/admin/adminEmpresas.fxml"); event.consume(); }
    @FXML private void onSalir(ActionEvent event) { Navegador.irA("views/admin/loginAdmin.fxml"); event.consume(); }
}
