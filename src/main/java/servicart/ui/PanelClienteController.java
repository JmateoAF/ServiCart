package servicart.ui;  // ajusta si tu paquete es otro

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PanelClienteController {
    //Sidebar
        @FXML private Button btnMisServicios;
        @FXML private Button btnCarrito;
        @FXML private Button btnPerfil;
    //Contenedor general de servicios
        @FXML private VBox contenedorServicios;
   //Etapa
   @FXML private VBox tarjetaEtapa;
    @FXML private Label lblEmpresaEtapa;
    @FXML private Label lblTipoEtapa;
    @FXML private Label lblEstadoEtapa;
    @FXML private Button btnToggleEtapa;
    @FXML private VBox paneCortadoEtapa;
    @FXML private Label lblCostoReactivacionEtapa;
    @FXML private VBox facturasEtapa;
    @FXML private Label lblPeriodoEtapa1;
    @FXML private Label lblVencimientoEtapa1;
    @FXML private Label lblCorteEtapa1;
    @FXML private Label lblMontoOriginalEtapa1;
    @FXML private HBox hboxMoraEtapa1;
    @FXML private Label lblMoraEtapa1;
    @FXML private TextField montoEtapa1;
    @FXML private Button btnAnadirEtapa1;

    //Centrosu
    @FXML private VBox tarjetaCentrosur;
    @FXML private Label lblEmpresaCentrosur;
    @FXML private Label lblTipoCentrosur;
    @FXML private Label lblEstadoCentrosur;
    @FXML private Button btnToggleCentrosur;
    @FXML private VBox paneCortadoCentrosur;
    @FXML private Label lblCostoReactivacionCentrosur;
    @FXML private VBox facturasCentrosur;
    @FXML private Label lblPeriodoCentrosur1;
    @FXML private Label lblVencimientoCentrosur1;
    @FXML private Label lblCorteCentrosur1;
    @FXML private Label lblMontoOriginalCentrosur1;
    @FXML private HBox hboxMoraCentrosur1;
    @FXML private Label lblMoraCentrosur1;
    @FXML private TextField montoCentrosur1;
    @FXML private Button btnAnadirCentrosur1;
    //FibraMax
    @FXML private VBox tarjetaFibramax;
    @FXML private Label lblEmpresaFibramax;
    @FXML private Label lblTipoFibramax;
    @FXML private Label lblEstadoFibramax;
    @FXML private Button btnToggleFibramax;
    @FXML private VBox paneCortadoFibramax;
    @FXML private Label lblCostoReactivacionFibramax;
    @FXML private VBox facturasFibramax;
    @FXML private Label lblPeriodoFibramax1;
    @FXML private Label lblVencimientoFibramax1;
    @FXML private Label lblCorteFibramax1;
    @FXML private Label lblMontoOriginalFibramax1;
    @FXML private HBox hboxMoraFibramax1;
    @FXML private Label lblMoraFibramax1;
    @FXML private TextField montoFibramax1;
    @FXML private Button btnAnadirFibramax1;
    //EMAC
    @FXML private VBox tarjetaEmac;
    @FXML private Label lblEmpresaEmac;
    @FXML private Label lblTipoEmac;
    @FXML private Label lblEstadoEmac;
    @FXML private Button btnToggleEmac;
    @FXML private VBox paneCortadoEmac;
    @FXML private Label lblCostoReactivacionEmac;
    @FXML private VBox facturasEmac;
    @FXML private Label lblPeriodoEmac1;
    @FXML private Label lblVencimientoEmac1;
    @FXML private Label lblCorteEmac1;
    @FXML private Label lblMontoOriginalEmac1;
    @FXML private HBox hboxMoraEmac1;
    @FXML private Label lblMoraEmac1;
    @FXML private TextField montoEmac1;
    @FXML private Button btnAnadirEmac1;
    // Carrito lateral
    @FXML private VBox paneCarritoVacio;
    @FXML private VBox listaCarrito;
    @FXML private Label lblTotal;
    @FXML private Button btnCheckout;
    @FXML private Button btnVaciarCarrito;
    //Label mensaje
    @FXML private Label lblMensaje;

    // ---- Métodos onAction (ya los tienes, solo los enumero) ----
    @FXML private void onMisServicios(ActionEvent event) { }
    @FXML private void onCarrito(ActionEvent event) { }
    @FXML private void onPerfil(ActionEvent event) { }
    @FXML private void onSalir(ActionEvent event) { }
    @FXML private void onToggleEtapa(ActionEvent event) { }
    @FXML private void onToggleCentrosur(ActionEvent event) { }
    @FXML private void onToggleFibramax(ActionEvent event) { }
    @FXML private void onToggleEmac(ActionEvent event) { }
    @FXML private void onAnadirEtapa1(ActionEvent event) { }
    @FXML private void onCheckout(ActionEvent event) { }
    @FXML private void onVaciarCarrito(ActionEvent event) { }

    public void onReactivarEtapa(ActionEvent actionEvent) {
    }

    public void onReactivarCentrosur(ActionEvent actionEvent) {
    }

    public void onAnadirCentrosur1(ActionEvent actionEvent) {
    }

    public void onReactivarFibramax(ActionEvent actionEvent) {
    }

    public void onAnadirFibramax1(ActionEvent actionEvent) {
    }

    public void onReactivarEmac(ActionEvent actionEvent) {
    }

    public void onAnadirEmac1(ActionEvent actionEvent) {
    }
}