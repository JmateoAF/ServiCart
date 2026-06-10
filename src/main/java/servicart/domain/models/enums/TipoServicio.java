package servicart.domain.models.enums;

public enum TipoServicio {
    AGUA(0), LUZ(1), BASURA(2), INTERNET(3);

    private final int codigo;

    TipoServicio(int codigo) {
        this.codigo = codigo;
    }
    public int getCodigo() {
        return codigo;
    }

    public static TipoServicio fromCodigo(int codigo) {
        for (TipoServicio ts : values()) if (ts.codigo == codigo) return ts;
        throw new IllegalArgumentException("Servicio no válido: " + codigo);
    }
}
