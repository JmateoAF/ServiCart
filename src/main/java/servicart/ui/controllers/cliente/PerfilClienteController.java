package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import servicart.domain.dtos.entradas.CancelarContratoDTOEntrada;
import servicart.domain.dtos.entradas.PerfilClienteDTOEntrada;
import servicart.domain.dtos.entradas.ContratoDTOEntrada;
import servicart.domain.dtos.retornos.ContratoDTORetorno;
import servicart.domain.dtos.retornos.PerfilClienteDTORetorno;
import servicart.domain.interfaces.ContratoCliente;
import servicart.domain.interfaces.PerfilCliente;
import servicart.ui.SesionCliente;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.ContratoMapperUI;
import servicart.ui.mappers.PerfilClienteMapperUI;
import servicart.ui.viewmodels.cliente.ContratoViewModel;
import servicart.ui.viewmodels.cliente.PerfilClienteViewModel;

import java.util.List;
import java.util.Optional;

public class PerfilClienteController {
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblEmailUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCelular;
    @FXML private TextField txtCedula;
    @FXML private Label lblMensaje;
    @FXML private VBox listaServicios;

    private final PerfilCliente perfilCliente;
    private final ContratoCliente contratoCliente;

    public PerfilClienteController(PerfilCliente perfilCliente, ContratoCliente contratoCliente) {
        this.perfilCliente = perfilCliente;
        this.contratoCliente = contratoCliente;
    }

    @FXML
    public void initialize() {
        cargarDatos();
        cargarContratos();
    }

    private void cargarDatos() {
        String cedula = SesionCliente.getCedulaActual();

        PerfilClienteDTOEntrada dtoEntrada = new PerfilClienteDTOEntrada(cedula);
        PerfilClienteDTORetorno dtoSalida = perfilCliente.buscarPerfil(dtoEntrada);

        if (dtoSalida == null) return;

        PerfilClienteViewModel clienteVM = PerfilClienteMapperUI.dtoAViewModel(dtoSalida);

        lblNombreUsuario.setText(clienteVM.getNombre());
        lblEmailUsuario.setText(clienteVM.getEmail());
        txtNombre.setText(clienteVM.getNombre());
        txtCedula.setText(clienteVM.getCedula());
        txtEmail.setText(clienteVM.getEmail());
        txtCelular.setText(clienteVM.getCelular());
    }

    private void cargarContratos() {
        String cedula = SesionCliente.getCedulaActual();

        List<ContratoDTORetorno> dtos = contratoCliente.listarContratos(new ContratoDTOEntrada(cedula));

        listaServicios.getChildren().clear();

        if (dtos.isEmpty()) {
            lblMensaje.setText("No tienes servicios contratados actualmente.");
            lblMensaje.setVisible(true);
            lblMensaje.setManaged(true);
            return;
        }

        lblMensaje.setVisible(false);
        lblMensaje.setManaged(false);
        lblMensaje.setVisible(false);

        for (ContratoDTORetorno dto : dtos) {
            ContratoViewModel vm = ContratoMapperUI.dtoAViewModel(dto);
            listaServicios.getChildren().add(crearTarjetaContrato(vm));
        }
    }

    private HBox crearTarjetaContrato(ContratoViewModel vm) {
        Label lblTitulo = new Label(vm.getEmpresa() + " — " + vm.getTipoServicio());
        lblTitulo.setStyle("-fx-text-fill: #e8c96d; -fx-font-weight: bold; -fx-font-size: 15;");

        Label lblDetalle = new Label(vm.getTarifaTexto() + "   ·   Desde " + vm.getFechaInicioTexto());
        lblDetalle.setStyle("-fx-text-fill: #999999; -fx-font-size: 15;");

        VBox info = new VBox(5, lblTitulo, lblDetalle);

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #2a1010; -fx-text-fill: #c0392b; " +
                "-fx-border-color: #c0392b; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
        btnCancelar.setOnAction(event -> { onCancelarContrato(vm.getId()); event.consume(); });

        HBox fila = new HBox(info, crearEspaciador(), btnCancelar);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setSpacing(15);
        fila.setPadding(new Insets(10, 15, 10, 15));
        fila.setStyle("-fx-background-color: #161616; -fx-border-color: #252525; " +
                "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");

        return fila;
    }

    private javafx.scene.layout.Region crearEspaciador() {
        javafx.scene.layout.Region espaciador = new javafx.scene.layout.Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);
        return espaciador;
    }

    private void onCancelarContrato(int idContrato) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        Navegador.estilizarDialogo(confirmacion);
        confirmacion.setTitle("Cancelar servicio");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que deseas cancelar este servicio? Esta acción no se puede deshacer");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            contratoCliente.cancelarContrato(new CancelarContratoDTOEntrada(idContrato));
            cargarContratos();
        }
    }

    @FXML private void onMisServicios(ActionEvent event) {
        Navegador.irA("views/cliente/panelCliente.fxml");
        event.consume();
    }

    @FXML private void onCarrito(ActionEvent event) {
        Navegador.irA("views/cliente/carrito.fxml");
        event.consume();
    }

    @FXML private void onSalir(ActionEvent event) {
        SesionCliente.cerrar();
        Navegador.irA("views/cliente/loginCliente.fxml");
        event.consume();
    }


}