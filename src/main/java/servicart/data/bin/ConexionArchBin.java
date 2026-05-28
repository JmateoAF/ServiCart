package servicart.data.bin;

public class ConexionArchBin {
    private static final String NOMBRE_ARCHIVO = "usuarios.txt";
    private static final String SEPARADOR = ",";  // CSV

    // Verifica si el archivo existe físicamente
    public static boolean existeArchivo() {
        // return new File(NOMBRE_ARCHIVO).exists();
        return false;
    }

    // Crea el archivo vacío si no existe (similar a inicializar base de datos)
    public static boolean crearArchivoSiNoExiste() {
        // Si no existe, crea un archivo vacío (y opcionalmente escribe una cabecera)
        // Retorna true si se creó o ya existía; false si hubo error.
        return false;
    }

    // Verifica si el archivo está vacío (sin líneas, o solo cabecera)
    public static boolean estaVacio() {
        // Abre el archivo, lee la primera línea, si no hay -> true
        return false;
    }
}
