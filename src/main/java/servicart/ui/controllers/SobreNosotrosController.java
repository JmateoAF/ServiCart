package servicart.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import servicart.ui.core.Navegador;

public class SobreNosotrosController {
    @FXML
    private void onVolver(ActionEvent event) {
        Navegador.irA("views/cliente/loginCliente.fxml");
    }
}