package servicart.entities.enums;

/* Enum que indica la causa de terminación de un contrato.
Se guarda como número en contratos.bin */

public enum CausaTerminacion {
    ACTIVO(0), // contrato vigente, sin terminar
    CLIENTE(1), // cliente solicitó la baja
    EMPRESA(2); // empresa canceló por mora prolongada

    private final int codigo;

    CausaTerminacion(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    /* Obtiene la causa de terminación desde un código leído del archivo.
    Si el código no es válido, lanza excepción */

    public static CausaTerminacion fromCodigo(int codigo) {
        for (CausaTerminacion ct : values())
            if (ct.codigo == codigo) return ct;

        throw new IllegalArgumentException("Causa de terminación no válida: " + codigo);
    }
}
