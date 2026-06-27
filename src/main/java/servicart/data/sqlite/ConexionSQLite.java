package servicart.data.sqlite;

import servicart.exceptions.PersistenciaException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionSQLite {
    private static final String URL = "jdbc:sqlite:sql/data.db";

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
            throw new PersistenciaException("Error al conectar durante la inicialización: ", e);
        }
    }

    private static void ejecutarScriptSQL(Connection con, String rutaArchivo) {
        try (Statement stmt = con.createStatement(); BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            StringBuilder sql = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                String t = linea.trim();
                if (t.startsWith("--") || t.isEmpty()) continue;
                sql.append(linea).append(' ');

                if (t.endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql.setLength(0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("No se encontró el script SQL: " + rutaArchivo, e);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al ejecutar el script SQL: " + rutaArchivo, e);
        }
    }
}