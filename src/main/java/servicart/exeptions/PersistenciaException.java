package servicart.exeptions;

import java.sql.SQLException;

public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String message, SQLException e) {
        super(message, e);
    }
}
