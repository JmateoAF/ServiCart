package servicart.data.bin;

import java.io.*;
import java.nio.file.*;
import java.util.function.Consumer;

public class ConexionBinario {
    private static final String NOMBRE_ARCHIVO = "usuarios.bin";

    private static void asegurarArchivoExiste() {
        Path ruta = Paths.get(NOMBRE_ARCHIVO);
        try {
            Files.createFile(ruta);
        } catch (FileAlreadyExistsException e) {
            // El archivo ya existe, no hace nada
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo: " + NOMBRE_ARCHIVO, e);
        }
    }

    public static DataInputStream abrirParaLectura() throws IOException {
        asegurarArchivoExiste();
        return new DataInputStream(new FileInputStream(NOMBRE_ARCHIVO));
    }

    public static boolean estaVacioArchivo() throws IOException {
        Path ruta = Paths.get(NOMBRE_ARCHIVO);
        return Files.size(ruta) == 0;
    }

    //Escribe datos de forma atómica usando un archivo temporal
    public static void guardarAtomicamente(Consumer<DataOutputStream> escritor) throws IOException {
        Path archivoOriginal = Paths.get(NOMBRE_ARCHIVO);
        Path archivoTemporal = null;
        try {
            archivoTemporal = Files.createTempFile("usuarios_temp", ".bin");
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivoTemporal.toFile()))) {
                escritor.accept(dos);
            }
            Files.move(archivoTemporal, archivoOriginal,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            if (archivoTemporal != null) {
                try { Files.deleteIfExists(archivoTemporal); } catch (IOException ignored) {}
            }
            throw e; // Lanzamos la excepción original sin envolver
        }
    }
}