package servicart.domain.models.enums;

public enum TipoValorFactura {
    FIJO(1), VARIABLE(2);

    private final int codigo;

    TipoValorFactura(int codigo) { this.codigo = codigo; }

    public int getCodigo() { return this.codigo; }

    public static TipoValorFactura valueOf(int codigo) {
        for (TipoValorFactura tipoValorFactura : TipoValorFactura.values())
            if (tipoValorFactura.getCodigo() == codigo) { return tipoValorFactura; }

        throw new IllegalArgumentException("Error en el tipo de factura" + codigo);
    }
}
