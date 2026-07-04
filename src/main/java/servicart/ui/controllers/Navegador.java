package servicart.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import servicart.domain.interfaces.LoginCliente;
import servicart.domain.services.LoginClienteImp;

import java.io.IOException;
import java.util.Objects;

public class Navegador {
    private static Stage stage;
    private static final LoginCliente loginCliente = new LoginClienteImp();


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

    public static Object crearControlador(Class<?> claseControlador) {
        if (claseControlador == LoginClienteController.class) return new LoginClienteController(loginCliente);

        try {
            return claseControlador.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + claseControlador.getName(), e);
        }
    }
}