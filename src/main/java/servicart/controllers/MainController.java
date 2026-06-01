package servicart.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class MainController {

    @FXML
    private void irASobreNosotros(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource("/views/sobreNosotros.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error al cambiar de vista: " + e.getMessage());
        }
    }
}