import servicart.data.sql.ConexionSQLite;
import servicart.data.sql.UsuarioSQLite;
import servicart.data.interfaces.InterfazUsuario;
import servicart.core.models.Usuario;

void main() {
    System.out.println("Iniciando pruebas de entorno...");

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