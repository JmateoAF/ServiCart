package servicart.domain.models.enums;

/**
 * Enum que indica la causa de terminación de un contrato.
 * ACTIVO=0 (el contrato sigue vigente, no hay terminación),
 * CLIENTE=1 (el cliente solicitó la baja),
 * EMPRESA=2 (la empresa canceló el servicio).
 * Se guarda como número en contratos.bin.
 */
public enum CausaTerminacion {
    ACTIVO(0),
    CLIENTE(1),
    EMPRESA(2);

    private final int codigo;

    /**
     * Constructor automático que asigna el código a cada causa.
     */
    CausaTerminacion(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el código para guardar en el archivo binario.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Obtiene la causa de terminación desde un código leído del archivo.
     * Si el código no es válido, lanza excepción.
     */
    public static CausaTerminacion fromCodigo(int codigo) {
        for (CausaTerminacion ct : values()) {
            if (ct.codigo == codigo) return ct;
        }
        throw new IllegalArgumentException("Causa de terminación no válida: " + codigo);
    }
}
