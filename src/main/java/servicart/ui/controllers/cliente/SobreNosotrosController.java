package servicart.ui.controllers.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import servicart.ui.controllers.Navegador;

public class SobreNosotrosController {
    @FXML
    private void onVolver(ActionEvent event) {
        Navegador.irA("views/cliente/loginCliente.fxml");
        event.consume();
    }
}