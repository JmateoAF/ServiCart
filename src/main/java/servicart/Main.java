import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import servicart.ui.controllers.Navegador;
import java.util.Objects;

//TRABAJO REALIZADO POR: JOSTIN AUCANCELA Y MARITZA QUISHPI

/* Punto de arranque
Configurar el patrón Observer en FacturacionService
Lanzar la interfaz gráfica */

void main() {
    Platform.startup(() -> {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource("views/cliente/loginCliente.fxml")));

            Stage stage = new Stage();
            stage.setTitle("ServiCart");
            Scene scene = new Scene(root);
            stage.setScene(scene);

            Navegador.inicializar(stage);

            stage.getIcons().add(new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("assets/icon/marcoDorado.png"), "Ícono no encontrado")));
            stage.setMinWidth(700);
            stage.setMinHeight(500);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error al iniciar la UI: " + e.getMessage());
        }
    });
}