package servicart.domain.models.enums;

public enum Empresa {
    ETAPA(1), CENTROSUR(2), EMAC(3), FIBRAMAX(4);

    private final int codigo;

    Empresa(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static Empresa fromCodigo(int codigo) {
        for (Empresa e : values()) if (e.codigo == codigo) return e;
        throw new IllegalArgumentException("Empresa no válida: " + codigo);
    }
}
