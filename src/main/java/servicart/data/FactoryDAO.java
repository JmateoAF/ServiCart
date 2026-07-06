package servicart.data;

import servicart.data.binary.*;
import servicart.data.interfaces.*;
import servicart.data.sqlite.*;
import servicart.entities.*;

/* Patrón Simple Factory para la capa de datos
 Centraliza la decisión de qué implementación (binaria o SQLite)
 se entrega a los servicios del dominio */

public class FactoryDAO {
    private static String baseDatosActual;

    public static void configurar(String nombreBd) {
        baseDatosActual = nombreBd;
        if ("SQLite".equalsIgnoreCase(nombreBd)) ConexionSQLite.inicializarBaseDeDatos();
        else if ("Binario".equalsIgnoreCase(nombreBd)) DatosSeeder.iniciar();
    }

    @SuppressWarnings("unchecked")
    public static <T> CrudDAO<T> getDAO(Class<T> entidad) {
        boolean sqlite = "SQLite".equalsIgnoreCase(baseDatosActual);

        if (entidad == Cliente.class) return (CrudDAO<T>) (sqlite ? new ClienteSQLiteDAO() : new ClienteBinarioDAO());

        if (entidad == Contrato.class) return (CrudDAO<T>) (sqlite ? new ContratoSQLiteDAO() : new ContratoBinarioDAO());

        if (entidad == Factura.class) {
            return (CrudDAO<T>) (sqlite ? new FacturaSQLiteDAO() : new FacturaBinarioDAO());
        }
        if (entidad == Abono.class) {
            return (CrudDAO<T>) (sqlite ? new AbonoSQLiteDAO() : new AbonoBinarioDAO());
        }
        if (entidad == Carrito.class) {
            return (CrudDAO<T>) (sqlite ? new CarritoSQLiteDAO() : new CarritoBinarioDAO());
        }
        if (entidad == Empresa.class) return (CrudDAO<T>) (sqlite ? new EmpresaSQLiteDAO() : new EmpresaBinarioDAO());

        if (entidad == CorteServicio.class) {
            return (CrudDAO<T>) (sqlite ? new CorteServicioSQLiteDAO() : new CorteServicioBinarioDAO());
        }
        if (entidad == InteresMora.class) {
            return (CrudDAO<T>) (sqlite ? new InteresMoraSQLiteDAO(): new InteresMoraBinarioDAO());
        }
        if (entidad == ServicioCatalogo.class) {
            return (CrudDAO<T>) (sqlite ? new ServicioCatalogoSQLiteDAO() : new ServicioCatalogoBinarioDAO());
        }

        return null;
    }

    public static AdminDAO<Administrador> getAdminDAO() {
        boolean sqlite = "SQLite".equalsIgnoreCase(baseDatosActual);
        return sqlite ? new AdminSQLiteDAO() : new AdminBinarioDAO();
    }
}
