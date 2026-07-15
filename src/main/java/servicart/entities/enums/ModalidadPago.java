package servicart.entities.enums;

public enum ModalidadPago {
    TC(0),
    TD(1),
    PAYPAL(2),
    TRANSFERENCIA(3),
    DEBITO(4);

    private final int codigo;

    ModalidadPago(int codigo) {
        this.codigo = codigo;
    }

    public static ModalidadPago fromCodigo(int codigo) {
        for (ModalidadPago ts : values())
            if (ts.codigo == codigo) return ts;

        throw new IllegalArgumentException("Modalidad de Pago no válido: " + codigo);
    }

    public int getCodigo() {
        return codigo;
    }
}