package servicart.domain.models.enums;

public enum CausaTerminacion {
    NOAPLICA(0), EMPRESA(1), CLIENTE(2);

    private final int codigo;

    CausaTerminacion(int codigo) { this.codigo = codigo; }

    public int getCodigo() { return this.codigo; }

    public static CausaTerminacion fromCodigo(int codigo) {
        for (CausaTerminacion causaTerminacion : CausaTerminacion.values())
            if (causaTerminacion.getCodigo() == codigo) return causaTerminacion;

        throw new IllegalArgumentException("Error en el código" + codigo);
    }
}
