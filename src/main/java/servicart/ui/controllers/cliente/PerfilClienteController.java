package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import servicart.domain.dtos.entradas.PerfilClienteDTOEntrada;
import servicart.domain.dtos.salidas.PerfilClienteDTOSalida;
import servicart.domain.interfaces.PerfilCliente;
import servicart.ui.SesionCliente;
import servicart.ui.controllers.Navegador;
import servicart.ui.mappers.PerfilClienteMapperUI;
import servicart.ui.viewmodels.cliente.PerfilClienteViewModel;

public class PerfilClienteController {
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblEmailUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCelular;
    @FXML private TextField txtCedula;

    private final PerfilCliente perfilCliente;

    public PerfilClienteController(PerfilCliente perfilCliente) {
        this.perfilCliente = perfilCliente;
    }

    @FXML
    public void initialize() { cargarDatos(); }

    private void cargarDatos() {
        String cedula = SesionCliente.getCedulaActual();

        PerfilClienteDTOEntrada dtoEntrada = new PerfilClienteDTOEntrada(cedula);
        PerfilClienteDTOSalida dtoSalida = perfilCliente.buscarPerfil(dtoEntrada);

        if (dtoSalida == null) return; // no debería pasar si ya inició sesión antes

        PerfilClienteViewModel clienteVM = PerfilClienteMapperUI.dtoAViewModel(dtoSalida);

        lblNombreUsuario.setText(clienteVM.getNombre());
        lblEmailUsuario.setText(clienteVM.getEmail());
        txtNombre.setText(clienteVM.getNombre());
        txtCedula.setText(clienteVM.getCedula());
        txtEmail.setText(clienteVM.getEmail());
        txtCelular.setText(clienteVM.getCelular());
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