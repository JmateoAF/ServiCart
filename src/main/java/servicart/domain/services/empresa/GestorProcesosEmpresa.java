package servicart.domain.services.empresa;

import java.util.concurrent.atomic.AtomicBoolean;

/* Única responsabilidad: garantizar que la gestión automática de la
   empresa (GestionAutomaticaEmpresaJob) arranque una sola vez durante
   la vida de la aplicación, y se detenga al cerrar.

   La primera ejecución del ciclo se corre de forma BLOQUEANTE antes de
   programar las corridas periódicas. Esto evita que la UI (p. ej. el
   panel de cliente) consulte el estado de facturas/cortes mientras el
   primer ciclo todavía está a mitad de camino en otro hilo*/

public class GestorProcesosEmpresa {
    private static final AtomicBoolean iniciado = new AtomicBoolean(false);
    private static GestionAutomaticaEmpresaJob job;

    public static void iniciarSiEsNecesario() {
        if (iniciado.compareAndSet(false, true)) {
            job = new GestionAutomaticaEmpresaJob();
            job.ejecutarCicloInicial(); // bloqueante: garantiza que la UI vea datos consistentes desde el primer login
            job.iniciar();              // programa las corridas periódicas normales (cada 24h)
        }
    }

    public static void detener() {
        if (job != null) job.detener();
    }

}