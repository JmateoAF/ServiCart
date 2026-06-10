import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import servicart.domain.models.entidades.Cliente;
import servicart.data.sql.ClienteSQLiteDAO;
import servicart.data.sql.ConexionSQLite;
import servicart.domain.interfaces.CrudDAO;

void main() {
    //Inicializando la interfaz grafica
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

    //Inicializando las capas
    CrudDAO<Cliente> clienteCrudDAO = new ClienteSQLiteDAO(); //Inicializamos la capa de datos, hacer inyección de dependencias
    Cliente cliente = new Cliente(clienteCrudDAO); //Conectamos la capa de dominio con la capa de datos, punteros a donde se crea la base de datos

    //Inicializamos la base de datos
    ConexionSQLite.inicializarBaseDeDatos();
}