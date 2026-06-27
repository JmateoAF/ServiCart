package servicart.exceptions;

import java.sql.SQLException;

public class ServiCartException extends RuntimeException {
    public ServiCartException(String message) { super(message); }
}
