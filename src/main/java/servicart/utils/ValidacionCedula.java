package servicart.utils;

public class ValidacionCedula {

    public static boolean validarCedulaEcuador(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        // 1. Comprobar que contenga solo números
        if (!cedula.matches("\\d+")) {
            return false;
        }

        // 2. Validar código de provincia (primeros dos dígitos entre 01 y 24, o 30)
        int provincia = Integer.parseInt(cedula.substring(0, 2));

        if ((provincia < 1 || provincia > 24) && provincia != 30) {
            return false;
        }

        // 3. Validar el tercer dígito (debe ser menor a 6 para personas naturales)
        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito >= 6) {
            return false;
        }

        // 4. Algoritmo de coeficientes (2.1.2.1...)
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;

        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            int producto = digito * coeficientes[i];

            if (producto >= 10) {
                producto -= 9;
            }
            suma += producto;
        }

        // 5. Validar dígito verificador (el décimo número)
        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        int decenaSuperior = ((suma + 9) / 10) * 10;
        int resultado = decenaSuperior - suma;

        if (resultado == 10) {
            resultado = 0;
        }

        return resultado == digitoVerificador;
    }
}