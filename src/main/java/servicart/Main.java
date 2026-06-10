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
import servicart.domain.services.ClienteServices;

void main() {
    //ENTORNO DE PRUEBAS

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

    //Inicializamos la base de datos
    ConexionSQLite.inicializarBaseDeDatos();

    //Inicializando las capas

    /* if(){ ESTE IF SERVIRÁ PARA EL CONTROLADOR QUE VERIFICA Q BASE DE DATOS VA A USAR EL CLIENTE, VARIABLE TIPO BANDERA
        SI EL USUARIO ESCOGIO SQL E INICIALIZAR SU BASE DE DATOS
    }else if(){
        SI EL USUARIO ESCOGIO BINARIOS E INICIALIZAR SU BASE DE DATOS
    }
    HAY Q INVESTIGAR SI EXISTE FUNCIONES ASYC EN JAVA O COMO PODER HACER, CAPAZ GEMINI Y DEEP AL FALLO

    POR EL MOMENTO MANEJARE SQL PARA PROBAR LAS COSAS

    TU MARY, TAMBIEN INSTANCIARAS PARA QUE PRUEBES Y DOCUMENTA LAS COSAS Q SEAN NECESARIOAS PARA EVITAR ERRORES
    */

    CrudDAO<Cliente> clienteCrudDAO = new ClienteSQLiteDAO(); //Inicializamos la capa de datos

    ClienteServices cliente = new ClienteServices(clienteCrudDAO); //Conectamos la capa de dominio con la capa de datos, punteros a donde se crea la base de datos

    Cliente mary = new Cliente("0106807365", "Maritza", "lyrax@gmail.com", "0963304126", 1); //CAMBIAR LOS DATOS SI SE QUIERE PONER MÁS EN LA BASE DE DATOS

    cliente.guardarCliente(mary); //NOTA: DEBERÍA FUNCIONAR, INYECCIÓN, APUNTA A LA DIRECCION DE MEMORIA Y GUARDA EN EL CONTRUCTOR
    //LA COMUNICACIÓN ENTRE CAPAS SOLO POR INTERFACES, SOLO LLAMO A LOS MÉTODOS DE LAS INTERFACES Y LUEGO EL OBJETO CREADO SABRA COMO ES LA IMPLEMENTACIÓN
    //ACTUALIZACIÓN: NO FUNCIONO XD
    //ACTUALIZACIÓN 2: YA FUNCIONO XD

    Optional<Cliente> clienteEncontrado = cliente.buscarId("0106807365");

    /* cliente.buscarId("0106807365").ifPresentOrElse(
            c -> System.out.println("Cliente encontrado: " + c.getNombre()),
            () -> System.out.println("Lo siento, ese cliente no existe en ServiCart")
    ); */

    if(clienteEncontrado.isPresent()){
        Cliente c = clienteEncontrado.get();

        System.out.println(c.getNombre() + "\n");

        c.setNombre("Maritza Quispi");

        cliente.actualizar(c);
    }else{
        System.out.println("No existe el cliente");
    }

    for(Cliente lista : cliente.buscarTodos())
        System.out.println("Cédula: " + lista.getCedula() + " | Nombre: " + lista.getNombre());

    System.out.println("\n");

    cliente.eliminar("0106807365");

    System.out.println("\n");

    for(Cliente lista : cliente.buscarTodos())
        System.out.println("Cédula: " + lista.getCedula() + " | Nombre: " + lista.getNombre());
}