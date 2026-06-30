package servicart.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import servicart.exceptions.ServiCartException;
import java.io.IOException;
import java.util.Objects;

//Gestiona la navegación entre vistas de forma centralizada.
public class Navegador {
    private static Stage stage;

    //Llamado una sola vez desde el Main al arrancar
    public static void inicializar(Stage s) { stage = s; }

    //Carga una nueva vista y la muestra en el Stage actual
    public static void irA(String rutaFXML) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource(rutaFXML)));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            throw new ServiCartException("No se pudo cargar la vista: " + rutaFXML);
        }
    }

    /* Carga una vista y devuelve su controller para configurarlo antes de mostrarlo
    Útil cuando hay que pasar datos al controller destino
    var ctrl = Navegador.irAConController("views/...", MiController.class);
    ctrl.setDatos(datos); */
    public static <T> T irAConController(String rutaFXML, Class<T> tipoController) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(ClassLoader.getSystemResource(rutaFXML)));
            Parent root = loader.load();
            stage.getScene().setRoot(root);
            return loader.getController();
        } catch (IOException e) {
            throw new ServiCartException("No se pudo cargar la vista: " + rutaFXML);
        }
    }
}
