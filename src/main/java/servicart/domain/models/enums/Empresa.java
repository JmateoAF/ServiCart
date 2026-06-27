package servicart.domain.models.enums;

/* Enum que representa las empresas proveedoras de servicios.
Cada constante tiene un código numérico único que se usa para persistir
en archivos binarios (en lugar de guardar el nombre completo).
Los valores son fijos según la especificación: */

public enum Empresa {
    ETAPA(0),
    CENTROSUR(1),
    EMAC(2),
    FIBRAMAX(3);

    private final int codigo;  // Código numérico asociado a la empresa

    /*
     * Constructor del enum. Se ejecuta UNA SOLA VEZ por cada constante
     * de forma automática cuando se carga la clase. No se puede invocar manualmente.
     *
     * @param codigo El código entero que identificará a esta empresa en el archivo binario.
     */

    Empresa(int codigo) {
        this.codigo = codigo;
    }

    /*
     * Devuelve el código numérico de la empresa.
     * Se usa al ESCRIBIR en el archivo binario: dos.writeInt(empresa.getCodigo());
     *
     * @return El código (1, 2, 3 o 4).
     */

    public int getCodigo() {
        return codigo;
    }

    /*
     * Convierte un código numérico (leído del archivo binario) en la constante correspondiente.
     * Se usa al LEER del archivo binario: Empresa e = Empresa.fromCodigo(dis.readInt());
     * Si el código no coincide con ninguna empresa, lanza una excepción porque
     * el archivo estaría corrupto o el código es inválido.
     *
     * @param codigo El número leído del binario.
     * @return La constante Empresa correspondiente.
     * @throws IllegalArgumentException si el código no es 1, 2, 3 o 4.
     */

    public static Empresa fromCodigo(int codigo) {
        for (Empresa e : values())       // Recorre todas las constantes del enum
            if (e.codigo == codigo) return e;     // Si encuentra el código, devuelve la constante

        // Si no lo encuentra, el archivo está dañado o el código es incorrecto
        throw new IllegalArgumentException("Empresa no válida: " + codigo);
    }
}