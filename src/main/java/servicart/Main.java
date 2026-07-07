//TRABAJO REALIZADO POR: JOSTIN AUCANCELA Y MARITZA QUISHPI

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import servicart.domain.services.GestorProcesosEmpresa;
import servicart.ui.controllers.Navegador;
import java.util.Objects;

void main() {
    Platform.startup(() -> {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(ClassLoader.getSystemResource("views/cliente/loginCliente.fxml"), "No se encontró el FXML inicial"));
            loader.setControllerFactory(Navegador::crearControlador);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("ServiCart");
            stage.setScene(new Scene(root));

            Navegador.inicializar(stage);
            stage.setOnCloseRequest(event -> { GestorProcesosEmpresa.detener(); event.consume(); });
            stage.getIcons().add(new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("assets/icon/marcoDorado.png"), "Icono no encontrado")));
            stage.setMinWidth(700);
            stage.setMinHeight(500);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    });
}
