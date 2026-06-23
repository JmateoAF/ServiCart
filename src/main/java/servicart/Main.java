import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import servicart.models.entidades.Cliente;
import servicart.data.sql.ClienteSQLiteDAO;
import servicart.data.sql.ConexionSQLite;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.services.ClienteServices;

void main() {
    //Inicializando la interfaz grafica
    Platform.startup(() -> {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemResource("views/cliente/loginCliente.fxml")));

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

    //Inicializamos la base de datos
    ConexionSQLite.inicializarBaseDeDatos();

    //CrudDAO<Cliente> clienteCrudDAO = new ClienteBinarioDAO(); //Inicializamos la capa de datos
    CrudDAO<Cliente> clienteCrudDAO = new ClienteSQLiteDAO(); //Inicializamos la capa de datos

    ClienteServices cliente = new ClienteServices(clienteCrudDAO); //Conectamos la capa de dominio con la capa de datos, punteros a donde se crea la base de datos

    Cliente mary = new Cliente("0106807365", "Maritza", "lyrax@gmail.com", "0963304126"); //CAMBIAR LOS DATOS SI SE QUIERE PONER MÁS EN LA BASE DE DATOS

    try {
        cliente.guardarCliente(mary);
    } catch (RuntimeException e) {
        System.out.println("Mostrar en pantalla error de que ya existe usuario");
    }

    Optional<Cliente> clienteEncontrado = cliente.buscarId("0106807365");

    cliente.buscarId("0106807365").ifPresentOrElse(
            c -> System.out.println("Cliente encontrado: " + c.getNombre()),
            () -> System.out.println("Lo siento, ese cliente no existe en ServiCart")
    );

    if (clienteEncontrado.isPresent()) {
        Cliente c = clienteEncontrado.get();

        System.out.println(c.getNombre() + "\n");

        c.setNombre("Maritza Quispi");

        cliente.actualizar(c);
    } else {
        System.out.println("No existe el cliente");
    }

    for (Cliente lista : cliente.buscarTodos())
        System.out.println("Cédula: " + lista.getCedula() + " | Nombre: " + lista.getNombre());

    System.out.println("\n");

    cliente.eliminar("0106807365");

    for (Cliente lista : cliente.buscarTodos())
        System.out.println("Cédula: " + lista.getCedula() + " | Nombre: " + lista.getNombre());
}