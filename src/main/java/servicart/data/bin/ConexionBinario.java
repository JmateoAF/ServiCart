package servicart.data.bin;

import java.io.*;
import java.nio.file.*;
import java.util.function.Consumer;

public class ConexionBinario {
    private final Path rutaArchivo;

    public ConexionBinario(String nombreArchivo) {
        this.rutaArchivo = Paths.get(nombreArchivo); // ruta relativa o absoluta
        asegurarArchivoExiste();
    }

    // Garantiza que el archivo exista; si ya está, no hace nada.
    private void asegurarArchivoExiste() {
        try {
            Files.createFile(rutaArchivo);            // crea el archivo vacío
        } catch (FileAlreadyExistsException e) {
            // ya existe, normal
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo: " + rutaArchivo, e);
        }
    }

    // Verifica si el archivo tiene tamaño 0 bytes.
    public boolean estaVacio() throws IOException {
        return Files.size(rutaArchivo) == 0;          // Files.size evita abrir flujo
    }
    // Lee un objeto serializado desde el archivo y lo devuelve con tipo genérico, siendo una lista.
    @SuppressWarnings("unchecked")                   // suprime aviso de cast no verificable
    public <T> T leerObjeto() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(rutaArchivo.toFile()))) { // try-with-resources cierra solo
            return (T) ois.readObject();
        }
    }

    // Guarda de forma atómica usando un Consumer que recibe un ObjectOutputStream.
    // El DAO decide qué escribir; aquí solo se garantiza la integridad.
    public void guardarAtomicamente(Consumer<ObjectOutputStream> escritor) throws IOException {
        Path temp = null;
        try {
            // Crea un archivo temporal único, evita colisiones
            temp = Files.createTempFile("temp_ser", ".bin");
            // try-with-resources cierra automáticamente el stream
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(temp.toFile()))) {
                escritor.accept(oos);                // el DAO escribe aquí
            }
            // Reemplaza el original de manera atómica (evita archivos corruptos)
            Files.move(temp, rutaArchivo,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            // Si algo falla, limpia el temporal
            if (temp != null) {
                try { Files.deleteIfExists(temp); } catch (IOException ignored) {}
            }
            throw e;
        }
    }
}