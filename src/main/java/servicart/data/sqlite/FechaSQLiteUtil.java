package servicart.data.sqlite;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Utilidad de la capa de datos para convertir fechas entre el formato
   de texto que usa SQLite ("yyyy-MM-dd HH:mm:ss") y LocalDateTime.
   Se centraliza aquí porque LocalDateTime.parse()/toString() por defecto
   usan formato ISO con 'T', que no coincide con el formato de datosPrueba.sql */
public class FechaSQLiteUtil {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime parsear(String texto) {
        return LocalDateTime.parse(texto, FORMATO);
    }

    public static String formatear(LocalDateTime fecha) {
        return fecha.format(FORMATO);
    }
}