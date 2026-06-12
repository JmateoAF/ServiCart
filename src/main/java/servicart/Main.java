
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import servicart.data.sql.ConexionSQLite;

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

    //CrudDAO<Cliente> clienteCrudDAO = new ClienteSQLiteDAO(); //Inicializamos la capa de datos

    //ClienteServices cliente = new ClienteServices(clienteCrudDAO); //Conectamos la capa de dominio con la capa de datos, punteros a donde se crea la base de datos

    //Cliente mary = new Cliente("0106807365", "Maritza", "lyrax@gmail.com", "0963304126", 1); //CAMBIAR LOS DATOS SI SE QUIERE PONER MÁS EN LA BASE DE DATOS

    //cliente.guardarCliente(mary); //NOTA: DEBERÍA FUNCIONAR, INYECCIÓN, APUNTA A LA DIRECCION DE MEMORIA Y GUARDA EN EL CONTRUCTOR
    //LA COMUNICACIÓN ENTRE CAPAS SOLO POR INTERFACES, SOLO LLAMO A LOS MÉTODOS DE LAS INTERFACES Y LUEGO EL OBJETO CREADO SABRA COMO ES LA IMPLEMENTACIÓN
    //ACTUALIZACIÓN: NO FUNCIONO XD
    //ACTUALIZACIÓN 2: YA FUNCIONO XD

    //Optional<Cliente> clienteEncontrado = cliente.buscarId("0106807365");

    /* cliente.buscarId("0106807365").ifPresentOrElse(
            c -> System.out.println("Cliente encontrado: " + c.getNombre()),
            () -> System.out.println("Lo siento, ese cliente no existe en ServiCart")
    ); */

    /* if(clienteEncontrado.isPresent()){
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

     */
}
/*
* PRUEBA DATOS BINARIOS
* */
/*package servicart;

import servicart.data.bin.*;
import servicart.domain.models.entidades.*;
import servicart.domain.models.enums.*;
import servicart.domain.models.servicios.ServicioCatalogo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        // 1. Inicializar DAOs
        ClienteBinarioDAO clienteDAO = new ClienteBinarioDAO();
        ContratoBinarioDAO contratoDAO = new ContratoBinarioDAO();
        FacturaBinarioDAO facturaDAO = new FacturaBinarioDAO();
        CorteServicioBinarioDAO corteDAO = new CorteServicioBinarioDAO();
        AbonoBinarioDAO abonoDAO = new AbonoBinarioDAO();
        InteresMoraBinarioDAO interesDAO = new InteresMoraBinarioDAO();
        CarritoBinarioDAO carritoDAO = new CarritoBinarioDAO();
        ServicioCatalogoBinarioDAO catalogoDAO = new ServicioCatalogoBinarioDAO(); // ya tiene datos iniciales

        System.out.println("=== PRUEBA DE PERSISTENCIA BINARIA ===\n");

        // 2. Crear y guardar clientes
        Cliente cl1 = new Cliente("12345678A", "Juan Pérez", "juan@mail.com", "0991234567");
        Cliente cl2 = new Cliente("87654321B", "María Gómez", "maria@mail.com", "0987654321");
        clienteDAO.save(cl1);
        clienteDAO.save(cl2);
        System.out.println("Clientes guardados.");

        // 3. Obtener servicios del catálogo (IDs 1=AGUA, 2=LUZ, 3=BASURA, 4=INTERNET)
        Optional<ServicioCatalogo> optAgua = catalogoDAO.findId("1");
        Optional<ServicioCatalogo> optLuz = catalogoDAO.findId("2");
        if (optAgua.isEmpty() || optLuz.isEmpty()) {
            System.err.println("Error: servicios no encontrados en catálogo.");
            return;
        }
        ServicioCatalogo servicioAgua = optAgua.get();
        ServicioCatalogo servicioLuz = optLuz.get();

        // 4. Crear contratos (fechas: inicio hoy, fin dentro de 1 año, terminación null)
        LocalDateTime hoy = LocalDateTime.now();
        LocalDateTime unAnio = hoy.plusYears(1);
        Contrato contrato1 = new Contrato(1, hoy, unAnio, null, servicioAgua, cl1);
        Contrato contrato2 = new Contrato(2, hoy, unAnio, null, servicioLuz, cl2);
        contratoDAO.save(contrato1);
        contratoDAO.save(contrato2);
        System.out.println("Contratos guardados.");

        // 5. Crear facturas asociadas a los contratos
        Factura factura1 = new Factura(1, hoy, hoy.plusDays(15), hoy.plusDays(5), 25.0, contrato1);
        Factura factura2 = new Factura(2, hoy, hoy.plusDays(15), hoy.plusDays(5), 45.0, contrato2);
        facturaDAO.save(factura1);
        facturaDAO.save(factura2);
        System.out.println("Facturas guardadas.");

        // 6. Crear cortes de servicio
        CorteServicio corte1 = new CorteServicio(1, hoy, null, 0.0, contrato1, factura1);
        CorteServicio corte2 = new CorteServicio(2, hoy, null, 0.0, contrato2, factura2);
        corteDAO.save(corte1);
        corteDAO.save(corte2);
        System.out.println("Cortes de servicio guardados.");

        // 7. Crear abonos
        Abono abono1 = new Abono(1, 25.0, hoy, true, factura1, ModalidadPago.TC);
        Abono abono2 = new Abono(2, 20.0, hoy, false, factura2, ModalidadPago.TRANSFERENCIA);
        abonoDAO.save(abono1);
        abonoDAO.save(abono2);
        System.out.println("Abonos guardados.");

        // 8. Crear intereses de mora (ejemplo para factura2 con retraso)
        InteresMora interes = new InteresMora(1, 5, 2.25, hoy, false, factura2);
        interesDAO.save(interes);
        System.out.println("Interés de mora guardado.");

        // 9. Crear carrito (asociado a cliente y abono)
        Carrito carrito = new Carrito(1, 20.0, cl2, abono2);
        carritoDAO.save(carrito);
        System.out.println("Carrito guardado.\n");

        // 10. Listar todas las entidades activas
        System.out.println("--- LISTADO INICIAL ---");
        listarClientes(clienteDAO);
        listarContratos(contratoDAO);
        listarFacturas(facturaDAO);
        listarCortes(corteDAO);
        listarAbonos(abonoDAO);
        listarIntereses(interesDAO);
        listarCarritos(carritoDAO);

        // 11. Pruebas de operaciones específicas
        System.out.println("\n--- OPERACIONES ---");

        // Cortar servicio (cambia estado a CORTADO)
        corteDAO.cortarServicio("1");
        System.out.println("CorteServicio ID 1 → CORTADO");

        // Reactivar servicio (cambia a ACTIVO)
        corteDAO.reactivar("1");
        System.out.println("CorteServicio ID 1 → REACTIVADO (ACTIVO)");

        // Pagar factura (cambia a PAGADA)
        facturaDAO.facturaPagada("1");
        System.out.println("Factura ID 1 → PAGADA");

        // Terminar contrato (causa CLIENTE)
        contratoDAO.terminarContrato("1", CausaTerminacion.CLIENTE);
        System.out.println("Contrato ID 1 → TERMINADO por CLIENTE");

        // Eliminación lógica de cliente (activo = 0)
        clienteDAO.delete("12345678A");
        System.out.println("Cliente 12345678A → eliminado lógicamente (activo=0)");

        // Actualizar un abono (cambiar monto)
        abono2.setMonto(30.0);
        abono2.setPagoRealizado(true);
        abonoDAO.update(abono2);
        System.out.println("Abono ID 2 actualizado (monto=30.0, pagado=true)");

        // 12. Listar después de las operaciones
        System.out.println("\n--- LISTADO DESPUÉS DE OPERACIONES ---");
        listarClientes(clienteDAO);
        listarContratos(contratoDAO);
        listarFacturas(facturaDAO);
        listarCortes(corteDAO);
        listarAbonos(abonoDAO);
        listarIntereses(interesDAO);
        listarCarritos(carritoDAO);

        // 13. Buscar por ID
        System.out.println("\n--- BÚSQUEDA POR ID ---");
        Optional<Factura> facturaEncontrada = facturaDAO.findId("2");
        facturaEncontrada.ifPresentOrElse(
                f -> System.out.println("Factura ID 2 encontrada - Estado: " + f.getEstado()),
                () -> System.out.println("Factura ID 2 no encontrada")
        );

        Optional<Cliente> clienteEliminado = clienteDAO.findId("12345678A");
        if (clienteEliminado.isEmpty()) {
            System.out.println("Cliente 12345678A no aparece en findAll (borrado lógico)");
        }

        System.out.println("\n=== FIN DE LA PRUEBA ===");
    }

    // Métodos auxiliares para listar (solo muestran algunos atributos)
    private static void listarClientes(ClienteBinarioDAO dao) {
        List<Cliente> lista = dao.findAll();
        System.out.println("Clientes activos (" + lista.size() + "):");
        lista.forEach(c -> System.out.println("  " + c.getCedula() + " - " + c.getNombre() + " (activo=" + c.getActivo() + ")"));
    }

    private static void listarContratos(ContratoBinarioDAO dao) {
        List<Contrato> lista = dao.findAll();
        System.out.println("Contratos activos (" + lista.size() + "):");
        lista.forEach(c -> System.out.println("  ID " + c.getId() + " - Causa: " + c.getCausaTerminacion()));
    }

    private static void listarFacturas(FacturaBinarioDAO dao) {
        List<Factura> lista = dao.findAll();
        System.out.println("Facturas activas (" + lista.size() + "):");
        lista.forEach(f -> System.out.println("  ID " + f.getId() + " - Estado: " + f.getEstado() + " - Valor: " + f.getValorTotal()));
    }

    private static void listarCortes(CorteServicioBinarioDAO dao) {
        List<CorteServicio> lista = dao.findAll();
        System.out.println("Cortes activos (" + lista.size() + "):");
        lista.forEach(c -> System.out.println("  ID " + c.getId() + " - EstadoCorte: " + c.getEstadoCorte()));
    }

    private static void listarAbonos(AbonoBinarioDAO dao) {
        List<Abono> lista = dao.findAll();
        System.out.println("Abonos (" + lista.size() + "):");
        lista.forEach(a -> System.out.println("  ID " + a.getId() + " - Monto: " + a.getMonto() + " - Pagado: " + a.isPagoRealizado()));
    }

    private static void listarIntereses(InteresMoraBinarioDAO dao) {
        List<InteresMora> lista = dao.findAll();
        System.out.println("Intereses de mora (" + lista.size() + "):");
        lista.forEach(i -> System.out.println("  ID " + i.getId() + " - Días retraso: " + i.getDiasRetraso() + " - Interés: " + i.getInteresAcumulado()));
    }

    private static void listarCarritos(CarritoBinarioDAO dao) {
        List<Carrito> lista = dao.findAll();
        System.out.println("Carritos (" + lista.size() + "):");
        lista.forEach(c -> System.out.println("  ID " + c.getId() + " - Monto abono: " + c.getMontoAbono()));
    }
}
*/
