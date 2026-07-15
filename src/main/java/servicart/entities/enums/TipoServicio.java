package servicart.entities.enums;

// Representa los tipos de servicios con un código numérico asociado
public enum TipoServicio {
    AGUA(0),
    LUZ(1),
    BASURA(2),
    INTERNET(3); // cada constante guarda su código

    private final int codigo; // código numérico del servicio

    // Constructor privado (los enums lo llaman automáticamente al definir las constantes)
    TipoServicio(int codigo) {
        this.codigo = codigo;
    }

    // Convierte un código numérico de vuelta a su enum correspondiente
    public static TipoServicio fromCodigo(int codigo) {
        for (TipoServicio ts : values()) // values() devuelve todas las constantes del enum
            if (ts.codigo == codigo) return ts;

        throw new IllegalArgumentException("Servicio no válido: " + codigo);
    }

    // Permite acceder al código asociado al tipo de servicio
    public int getCodigo() {
        return codigo;
    }
}
