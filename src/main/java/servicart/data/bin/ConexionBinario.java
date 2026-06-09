package servicart.data.bin;

import java.io.*;
import java.nio.file.*;
import java.util.function.Consumer;

public class ConexionBinario {
    private final Path rutaArchivo;

    public ConexionBinario(String nombreArchivo) {
        this.rutaArchivo = Paths.get(nombreArchivo);
        asegurarArchivoExiste();
    }

    private void asegurarArchivoExiste() {
        try {
            Files.createFile(rutaArchivo);
        } catch (FileAlreadyExistsException e) {
            // El archivo ya existe, no hace nada
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo: " + rutaArchivo, e);
        }
    }

    public DataInputStream abrirParaLectura() throws IOException {
        return new DataInputStream(new FileInputStream(rutaArchivo.toFile()));
    }

    public boolean estaVacio() throws IOException {
        return Files.size(rutaArchivo) == 0;
    }

    public void guardarAtomicamente(Consumer<DataOutputStream> escritor) throws IOException {
        Path archivoTemporal = null;
        try {
            archivoTemporal = Files.createTempFile("temp_bin", ".bin");
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivoTemporal.toFile()))) {
                escritor.accept(dos);
            }
            Files.move(archivoTemporal, rutaArchivo,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            if (archivoTemporal != null) {
                try { Files.deleteIfExists(archivoTemporal); } catch (IOException ignored) {}
            }
            throw e;
        }
    }
}