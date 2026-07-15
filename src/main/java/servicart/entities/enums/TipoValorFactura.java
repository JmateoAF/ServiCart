package servicart.entities.enums;

public enum TipoValorFactura {
    FIJO(0),
    VARIABLE(1);

    private final int codigo;

    TipoValorFactura(int codigo) {
        this.codigo = codigo;
    }

    public static TipoValorFactura fromCodigo(int codigo) {
        for (TipoValorFactura tipoValorFactura : TipoValorFactura.values())
            if (tipoValorFactura.getCodigo() == codigo) return tipoValorFactura;

        throw new IllegalArgumentException("Error en el tipo de factura" + codigo);
    }

    public int getCodigo() {
        return this.codigo;
    }
}
