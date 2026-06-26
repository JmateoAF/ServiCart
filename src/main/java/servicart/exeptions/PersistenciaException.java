package servicart.exeptions;

public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String message) {
        super(message);
    }
}
