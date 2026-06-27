package servicart.exceptions;

public class EntidadDuplicadaException extends ServiCartException {
    public EntidadDuplicadaException(String id) {
        super("Ya existe un registro con el identificador: " + id, e);
    }
}
