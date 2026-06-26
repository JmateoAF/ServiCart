package servicart.exeptions;

public class EntidadNoEncontradaException extends RuntimeException {
    public EntidadNoEncontradaException(String message) {
        super(message);
    }
}
