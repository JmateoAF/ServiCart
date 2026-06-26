package servicart.data;

import servicart.data.binary.*;
import servicart.data.interfaces.AdminDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.data.sqlite.AdminSQLiteDAO;
import servicart.data.sqlite.ClienteSQLiteDAO;
import servicart.domain.models.entidades.*;
import servicart.domain.models.catalog.ServicioCatalogo;

/* Patrón Simple Factory para la capa de datos.
 Centraliza la decisión de qué implementación (binaria o SQLite)
 se entrega a los servicios del dominio
 Uso en Main:
 DAOFactory.configurar(Estrategia.SQLITE);
 CrudDAO<Cliente> dao = DAOFactory.clienteDAO();
 ClienteService service = new ClienteService(dao); */

public class FactoryDAO {
    public enum Estrategia { BINARIO, SQLITE }
    private static Estrategia estrategia = Estrategia.SQLITE;

    public static void configurar(Estrategia e) { estrategia = e; }

    public static Estrategia getEstrategia() { return estrategia; }

    public static CrudDAO<Cliente> clienteDAO() {
        return switch (estrategia) {
            case BINARIO -> new ClienteBinarioDAO();
            case SQLITE  -> new ClienteSQLiteDAO();
        };
    }

    // ── Entidades solo con implementación binaria (SQLite pendiente) ─────

    public static CrudDAO<Factura> facturaDAO() {
        return new FacturaBinarioDAO();
    }

    public static CrudDAO<Contrato> contratoDAO() {
        return new ContratoBinarioDAO();
    }

    public static CrudDAO<Abono> abonoDAO() {
        return new AbonoBinarioDAO();
    }

    public static CrudDAO<Carrito> carritoDAO() {
        return new CarritoBinarioDAO();
    }

    public static CrudDAO<CorteServicio> corteServicioDAO() {
        return new CorteServicioBinarioDAO();
    }

    public static CrudDAO<InteresMora> interesMoraDAO() {
        return new InteresMoraBinarioDAO();
    }

    public static CrudDAO<ServicioCatalogo> servicioCatalogoDAO() {
        return new ServicioCatalogoBinarioDAO();
    }

    // ── Solo SQLite (credenciales de admin en BD, no en archivos) ───────

    public static AdminDAO<Administrador> adminDAO() {
        return new AdminSQLiteDAO();
    }
}
