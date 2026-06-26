package servicart.data.binary;

import java.io.*;
import java.nio.file.*;
import java.util.function.Consumer;

/**
 * Capa de infraestructura para manejo de archivos binarios.
 * No conoce el formato de los datos internos; trabaja únicamente con bytes.
 */
public class ConexionBinario {
    private final Path rutaArchivo;

    public ConexionBinario(String nombreArchivo) {
        this.rutaArchivo = Paths.get(nombreArchivo);
        asegurarArchivoExiste();
    }

    /** Garantiza que el archivo y sus directorios padres existan. */
    private void asegurarArchivoExiste() {
        try {
            Path parent = rutaArchivo.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(rutaArchivo);
        } catch (FileAlreadyExistsException e) {
            // ya existe, no se hace nada
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo: " + rutaArchivo, e);
        }
    }

    /** Indica si el archivo tiene tamaño cero. */
    public boolean estaVacio() throws IOException {
        return Files.size(rutaArchivo) == 0;
    }

    /**
     * Abre un flujo de entrada de bajo nivel sobre el archivo.
     * El llamador es responsable de envolverlo con ObjectInputStream si lo requiere.
     */
    public InputStream newInputStream() throws IOException {
        return Files.newInputStream(rutaArchivo);
    }

    /**
     * Escribe contenido en el archivo de forma atómica.
     * Recibe un {@link Consumer} que trabaja sobre un OutputStream crudo (sin serialización).
     * Así la clase ignora completamente el formato de los datos.
     */
    public void escribirAtomicamente(Consumer<OutputStream> escritor) throws IOException {
        Path temp = null;
        try {
            // temporal en el mismo directorio para garantizar movimiento atómico
            Path dir = rutaArchivo.toAbsolutePath().getParent();
            if (dir == null) {
                dir = Paths.get(".");
            }
            temp = Files.createTempFile(dir, "temp_ser", ".bin");
            try (OutputStream os = Files.newOutputStream(temp)) {
                escritor.accept(os);   // el DAO escribe los bytes que considere
            }
            Files.move(temp, rutaArchivo,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            if (temp != null) {
                try { Files.deleteIfExists(temp); } catch (IOException ignored) {}
            }
            throw e;
        }
    }
}