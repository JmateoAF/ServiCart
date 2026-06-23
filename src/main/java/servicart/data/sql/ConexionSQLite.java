package servicart.data.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionSQLite {
    private static final String URL = "JDBC:sqlite:sql/data.db";

    // Solo da conexiones limpias
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Se ejecuta una sola vez desde el Main
    public static void inicializarBaseDeDatos() {
        // El try-with-resources asegura que esta conexión temporal se cierre al terminar
        try (Connection con = conectar()) {
            ejecutarScriptSQL(con, "sql/dbsetup.sql");
            ejecutarScriptSQL(con, "sql/datosprueba.sql");
            System.out.println("Base de datos inicializada con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al conectar durante la inicialización: " + e.getMessage());
        }
    }

    private static void ejecutarScriptSQL(Connection con, String rutaArchivo) {
        try (Statement stmt = con.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

            StringBuilder sql = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().startsWith("--") || linea.trim().isEmpty()) {
                    continue;
                }
                sql.append(linea);
                if (linea.trim().endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql.setLength(0);
                }
            }
        } catch (IOException | SQLException e) {
            System.out.println("Error con " + rutaArchivo + ": " + e.getMessage());
        }
    }
}