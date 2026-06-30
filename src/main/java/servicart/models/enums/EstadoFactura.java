package servicart.models.enums;

public enum EstadoFactura {
    PENDIENTE(0),
    PAGADA(1),
    VENCIDA(2);

    private final int codigo;

    EstadoFactura(int codigo) { this.codigo = codigo; }

    public int getCodigo() { return this.codigo; }

    public static EstadoFactura fromCodigo(int codigo) {
        for (EstadoFactura estado : EstadoFactura.values())
            if (estado.getCodigo() == codigo) return estado;

        throw new IllegalArgumentException("Error en el código" + codigo);
    }
}
