import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import servicart.data.CatalogoSeeder;
import servicart.data.FactoryDAO;
import servicart.data.sqlite.ConexionSQLite;
import servicart.ui.core.GestorNotificacion;
import servicart.ui.core.Navegador;
import java.util.Objects;

//TRABAJO REALIZADO POR: JOSTIN AUCANCELA Y MARITZA QUISHPI

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
    GestorNotificacion.inicializar();

    //Lanzar JavaFX
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