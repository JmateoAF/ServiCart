import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import servicart.data.CatalogoSeeder;
import servicart.data.FactoryDAO;
import servicart.data.sqlite.ConexionSQLite;
import servicart.domain.models.entities.NotificadorEmail;
import servicart.domain.models.entities.NotificadorSMS;
import servicart.domain.models.entities.NotificadorPantalla;
import servicart.domain.services.FacturacionService;
import servicart.ui.Navegador;
import java.util.Objects;

/* Punto de arranque
Inicializar la base de datos SQLite
Sembrar datos del catálogo si están vacíos
Configurar el patrón Observer en FacturacionService
Lanzar la interfaz gráfica
Cambiar de SQLite a Binario: DAOFactory.configurar(Estrategia.BINARIO) */

void main() {
    //Inicializar SQLite
    ConexionSQLite.inicializarBaseDeDatos();

    //Sembrar catálogo de servicios en archivo binario (si está vacío)
    new CatalogoSeeder(FactoryDAO.servicioCatalogoDAO()).sembrar();

    //Configurar Observer: al emitir facturas se notifica por email + SMS
    FacturacionService facturacionService = new FacturacionService(FactoryDAO.facturaDAO());
    facturacionService.agregarObservador(new NotificadorEmail());
    facturacionService.agregarObservador(new NotificadorSMS());
    facturacionService.agregarObservador(new NotificadorPantalla(msg -> System.out.println("[PANTALLA] " + msg)));

    //Lanzar JavaFX
    Platform.startup(() -> {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource("views/cliente/loginCliente.fxml")));
            Stage stage = new Stage();
            stage.setTitle("ServiCart");
            stage.setScene(new Scene(root));
            stage.setMinWidth(700);
            stage.setMinHeight(500);
            stage.setMaximized(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("assets/icon/marcoDorado.png"))));

            // Inicializar el Navegador con el Stage principal
            Navegador.inicializar(stage);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error al iniciar la UI: " + e.getMessage());
        }
    });
}