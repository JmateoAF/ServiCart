package servicart.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import servicart.domain.interfaces.LoginCliente;
import servicart.domain.services.LoginClienteImp;
import servicart.ui.viewmodels.cliente.PerfilClienteViewModel;

import java.io.IOException;
import java.util.Objects;

public class Navegador {
    private static Stage stage;
    private static final LoginCliente loginCliente = new LoginClienteImp();

    private static PerfilClienteViewModel clientePendiente;
    private static String baseDatosPendiente;

    public static void inicializar(Stage stage) { Navegador.stage = stage; }

    public static void irA(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(ClassLoader.getSystemResource(rutaFXML)));
            loader.setControllerFactory(Navegador::crearControlador);
            Parent root = loader.load();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void irA(String rutaFXML, PerfilClienteViewModel cliente, String baseDatos) {
        clientePendiente = cliente;
        baseDatosPendiente = baseDatos;
        irA(rutaFXML);
    }

    public static PerfilClienteViewModel getClientePendiente() {
        PerfilClienteViewModel c = clientePendiente;
        clientePendiente = null;
        return c;
    }

    public static String getBaseDatosPendiente() {
        String bd = baseDatosPendiente;
        baseDatosPendiente = null;
        return bd;
    }

    public static Object crearControlador(Class<?> claseControlador) {
        if (claseControlador == LoginClienteController.class) return new LoginClienteController(loginCliente);

        try {
            return claseControlador.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + claseControlador.getName(), e);
        }
    }
}