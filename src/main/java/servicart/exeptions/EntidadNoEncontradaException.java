package servicart.exeptions;

import java.sql.SQLException;

public class EntidadNoEncontradaException extends RuntimeException {
    public EntidadNoEncontradaException(String message) { super(message); }
}
