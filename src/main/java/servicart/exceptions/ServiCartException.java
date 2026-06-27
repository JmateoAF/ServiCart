package servicart.exceptions;

import java.io.IOException;

public class ServiCartException extends RuntimeException {
    public ServiCartException(String message, IOException e) {
        super(message);
    }
}
