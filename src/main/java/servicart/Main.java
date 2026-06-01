import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import servicart.data.sql.ConexionSQLite;

void main() {
    System.out.println("Lanzando interfaz gráfica...");

    // Esto enciende JavaFX directamente sin usar clases extras
    Platform.startup(() -> {
        try {
            // 1. Buscamos y cargamos tu archivo FXML
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource("views/main.fxml")));

            // 2. Creamos y mostramos la ventana ahí mismo
            Stage stage = new Stage();
            stage.setTitle("ServiCart");
            stage.setScene(new Scene(root));

            // Forzar dimensiones de la pantalla completa de forma manual
            javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            stage.show();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    });

    System.out.println("\nIniciando pruebas de entorno...");

    //Inicializamos la base de datos y cargamos los archivos .sql
    ConexionSQLite.inicializarBaseDeDatos();


}