package servicart.domain.services.empresa;

import java.util.concurrent.atomic.AtomicBoolean;

/* Única responsabilidad: garantizar que la gestión automática de la
   empresa (GestionAutomaticaEmpresaJob) arranque una sola vez durante
   la vida de la aplicación, y se detenga al cerrar */

public class GestorProcesosEmpresa {
    private static final AtomicBoolean iniciado = new AtomicBoolean(false);
    private static GestionAutomaticaEmpresaJob job;

    public static void iniciarSiEsNecesario() {
        if (iniciado.compareAndSet(false, true)) {
            job = new GestionAutomaticaEmpresaJob();
            job.iniciar();
        }
    }

    public static void detener() {
        if (job != null) job.detener();
    }

}