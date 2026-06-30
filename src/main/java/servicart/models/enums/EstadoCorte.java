package servicart.models.enums;

public enum EstadoCorte {
    ACTIVO(0),
    CORTADO(1),
    TERMINADO(2);

    private final int codigo;

    EstadoCorte(int codigo) { this.codigo = codigo; }

    public int getCodigo() { return this.codigo; }

    public static EstadoCorte fromCodigo(int codigo) {
        for (EstadoCorte estado : EstadoCorte.values())
            if (estado.getCodigo() == codigo) return estado;

        throw new IllegalArgumentException("Error en el código: " + codigo);
    }
}
