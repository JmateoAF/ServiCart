package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import servicart.data.FactoryDAO;
import servicart.models.entities.Cliente;
import servicart.domain.services.ClienteServices;
import servicart.dtos.ClienteDTO;
import servicart.exceptions.ServiCartException;
import servicart.ui.core.Navegador;
import servicart.ui.core.Sesion;

public class PerfilController {
    public TextField txtTelefono;
    @FXML private VBox listaServicios;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblEmailUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCelular;
    @FXML private TextField txtCedula;
    @FXML private Label lblMensaje;

    private ClienteServices clienteService;

    @FXML
    public void initialize() {
        //clienteService = new ClienteServices(FactoryDAO.clienteDAO());
        cargarDatos();
    }

    private void cargarDatos() {
        ClienteDTO c = Sesion.getClienteDTO();
        if (c == null) return;

        lblNombreUsuario.setText(c.nombre());
        lblEmailUsuario.setText(c.email());
        txtNombre.setText(c.nombre());
        txtEmail.setText(c.email());
        txtCelular.setText(c.celular());
        txtCedula.setText(c.cedula());
        txtCedula.setEditable(false); // la cédula no se puede cambiar
        lblMensaje.setVisible(false);
    }

    @FXML
    private void onGuardarDatos(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String celular = txtCelular.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || celular.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios");

            return;
        }

        try {
            Cliente entidad = Sesion.getClienteEntity();
            entidad.setNombre(nombre);
            entidad.setEmail(email);
            entidad.setCelular(celular);

            clienteService.actualizar(entidad);
            Sesion.iniciar(entidad); // refresca el DTO en sesión
            mostrarMensaje("✓ Datos actualizados correctamente");
            cargarDatos();
        } catch (ServiCartException e) {
            mostrarMensaje("Error al guardar: " + e.getMessage());
        }
    }

    @FXML private void onMisServicios(ActionEvent e) { Navegador.irA("views/cliente/panelCliente.fxml"); }
    @FXML private void onCarrito(ActionEvent e) { Navegador.irA("views/cliente/carrito.fxml"); }
    @FXML private void onSalir(ActionEvent e) { Sesion.cerrar(); Navegador.irA("views/cliente/loginCliente.fxml"); }

    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setVisible(true);
    }
}