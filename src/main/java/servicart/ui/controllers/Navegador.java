package servicart.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import servicart.domain.interfaces.*;
import servicart.domain.services.admin.LoginAdminImp;
import servicart.domain.services.cliente.*;
import servicart.ui.controllers.admin.LoginAdminController;
import servicart.ui.controllers.cliente.*;

import java.io.IOException;
import java.util.Objects;

public class Navegador {
    private static Stage stage;
    private static final LoginCliente loginCliente = new LoginClienteImp();
    private static final PanelCliente panelCliente = new PanelClienteImp();
    private static final PerfilCliente perfilCliente = new PerfilClienteImp();
    private static final ContratoCliente contratoCliente = new ContratoClienteImp();
    private static final CarritoCliente carritoCliente = new CarritoClienteImp();
    private static final Checkout checkout = new CheckoutImp();
    private static final LoginAdmin loginAdmin = new LoginAdminImp();

    public static void inicializar(Stage stage) { Navegador.stage = stage; }

    public static void irA(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(ClassLoader.getSystemResource(rutaFXML)));
            loader.setControllerFactory(Navegador::crearControlador);
            Parent root = loader.load();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar" + rutaFXML, e);
        }
    }

    public static Object crearControlador(Class<?> claseControlador) {
        if (claseControlador == LoginClienteController.class) return new LoginClienteController(loginCliente);

        if (claseControlador == PanelClienteController.class) return new PanelClienteController(panelCliente);

        if (claseControlador == PerfilClienteController.class) return new PerfilClienteController(perfilCliente, contratoCliente);

        if (claseControlador == LoginAdminController.class) return new LoginAdminController(loginAdmin);

        if (claseControlador == CarritoController.class) return new CarritoController(carritoCliente);

        if (claseControlador == CheckoutController.class) return new CheckoutController(checkout);

        try {
            return claseControlador.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + claseControlador.getName(), e);
        }
    }
}