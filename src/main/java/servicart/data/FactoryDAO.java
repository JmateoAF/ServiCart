package servicart.data;

import servicart.data.binary.ClienteBinarioDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.data.sqlite.ClienteSQLiteDAO;
import servicart.data.sqlite.ConexionSQLite;
import servicart.entities.Cliente;

import java.util.HashMap;
import java.util.Map;
/* Patrón Simple Factory para la capa de datos
 Centraliza la decisión de qué implementación (binaria o SQLite)
 se entrega a los servicios del dominio */

public class FactoryDAO {
    private static final Map<Class<?>, CrudDAO<?>> daoMap = new HashMap<>();

    public static void configurar(String nombreBd) {
        daoMap.clear(); // Limpia registros anteriores

        if ("SQLite".equalsIgnoreCase(nombreBd)) {
            ConexionSQLite.inicializarBaseDeDatos(); // Crea las tablas si no existen
            daoMap.put(Cliente.class, new ClienteSQLiteDAO()); // ← REGISTRO SQLite
        } else if ("Binario".equalsIgnoreCase(nombreBd)) {
            DatosSeeder.iniciar(); // Carga los datos de prueba
            daoMap.put(Cliente.class, new ClienteBinarioDAO()); // ← REGISTRO Binario
        } else {
            throw new IllegalArgumentException("Tipo de base de datos no soportado: " + nombreBd);
        }
    }

    //daoMap: Un mapa que asocia clases de entidades con sus DAOs correspondientes.
    //Sirve para no crear un getDao para cada entidad

    // METODO GENÉRICO: Pide la clase de la entidad y devuelve el DAO correspondiente
    @SuppressWarnings("unchecked")
    public static <T> CrudDAO<T> getDAO(Class<T> entityClass) {
        CrudDAO<T> dao = (CrudDAO<T>) daoMap.get(entityClass);
        if (dao == null) {
            throw new IllegalStateException(
                    "No hay DAO registrado para la entidad: " + entityClass.getSimpleName() +
                            ". ¿Ejecutaste configurar() primero?"
            );
        }
        return dao;
    }


}
