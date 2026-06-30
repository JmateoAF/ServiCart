package servicart.data;

import servicart.data.sqlite.ConexionSQLite;

/* Patrón Simple Factory para la capa de datos
 Centraliza la decisión de qué implementación (binaria o SQLite)
 se entrega a los servicios del dominio */

public class FactoryDAO {
    public static void configurar(String nombreBd) {
        if ("SQLite".equalsIgnoreCase(nombreBd)) ConexionSQLite.inicializarBaseDeDatos();
        else if ("Binario".equalsIgnoreCase(nombreBd)) DatosSeeder.iniciar();
    }
}
