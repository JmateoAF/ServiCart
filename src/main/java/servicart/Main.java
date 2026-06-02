import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import servicart.data.sql.ConexionSQLite;

void main() {
    System.out.println("Lanzando interfaz gráfica");

    Platform.startup(() -> {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource("views/main.fxml")));

            Stage stage = new Stage();
            stage.setTitle("ServiCart");

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("assets/icon/marcoDorado.png"))));

            stage.setMinWidth(700);
            stage.setMinHeight(500);

            stage.sizeToScene();
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    });

    System.out.println("\nIniciando pruebas de entorno");

    //Inicializamos la base de datos y cargamos los archivos .sql
    ConexionSQLite.inicializarBaseDeDatos();
}