package servicart.exceptions;

import java.sql.SQLException;

public class EntidadDuplicadaException extends RuntimeException {
    public EntidadDuplicadaException(String id, SQLException e) { super("Ya existe un registro con el identificador: " + id, e); }
}
