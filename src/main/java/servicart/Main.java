import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import servicart.data.sql.ConexionSQLite;
import servicart.data.sql.UsuarioSQLite;
import servicart.data.interfaces.InterfazUsuario;
import servicart.core.models.Usuario;

void main() {
    System.out.println("Lanzando interfaz gráfica...");

    // Esto enciende JavaFX directamente sin usar clases extras
    Platform.startup(() -> {
        try {
            // 1. Buscamos y cargamos tu archivo FXML
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource("views/main.fxml")));

            // 2. Creamos y mostramos la ventana ahí mismo
            Stage stage = new Stage();
            stage.setTitle("ServiCart");
            stage.setScene(new Scene(root));

            // Forzar dimensiones de la pantalla completa de forma manual
            javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            stage.show();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    });

    System.out.println("\nIniciando pruebas de entorno...");

    // 1. Inicializamos la base de datos y cargamos los archivos .sql (dbsetup y datosprueba)
    ConexionSQLite.inicializarBaseDeDatos();

    System.out.println("\n--- Iniciando Prueba del Patrón DAO con SQLite ---");

    // 2. Programación orientada a interfaces: Variable de tipo Interfaz, instancia de la clase SQLite
    InterfazUsuario usuarioSQL = new UsuarioSQLite();

    // 3. Creamos el usuario de prueba (Modelo)
    // Nota: Asegúrate de que el constructor de tu clase Usuario reciba los parámetros en este orden.
    Usuario nuevoUsuario = new Usuario(2, "0107778889", "Maritza Quispi");

    // 4. Guardamos en la base de datos usando el DAO
    System.out.println("\nIntentando guardar usuario de prueba...");
    boolean guardado = usuarioSQL.insertar(nuevoUsuario);

    if (guardado) {
        System.out.println("¡Usuario guardado con éxito!");
    } else {
        System.out.println("No se pudo guardar (quizás la cédula o el ID ya existen).");
    }
}