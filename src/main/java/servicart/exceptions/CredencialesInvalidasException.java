package servicart.exceptions;

public class CredencialesInvalidasException extends ServiCartException {
    public CredencialesInvalidasException() { super("Usuario o contraseña incorrectos"); }
}
