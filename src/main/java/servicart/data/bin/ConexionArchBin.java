package servicart.data.bin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConexionArchBin {
    private static final String NOMBRE_ARCHIVO = "usuarios.bin";

    private static void asegurarArchivo() {
        Path ruta = Paths.get(NOMBRE_ARCHIVO);
        if (Files.notExists(ruta)) {
            try {
                Files.createFile(ruta);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static DataInputStream abrirParaLectura() throws IOException {
        asegurarArchivo();
        return new DataInputStream(new FileInputStream(NOMBRE_ARCHIVO));
    }

    public static DataOutputStream abrirParaEscritura() throws IOException {
        asegurarArchivo();
        // Si el archivo no existe, se crea automáticamente al abrir FileOutputStream
        return new DataOutputStream(new FileOutputStream(NOMBRE_ARCHIVO));
    }

    // Verifica si el archivo está vacío (sin líneas, o solo cabecera)
    public static boolean estaVacio() throws IOException {
        Path ruta = Paths.get(NOMBRE_ARCHIVO);
            return Files.size(ruta) == 0;
    }
}
