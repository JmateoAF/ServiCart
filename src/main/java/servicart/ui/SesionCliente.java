package servicart.ui;

//Clase utilitaria para saber qué usuario está en ese momento
public class SesionCliente {
    private static String cedulaActual;

    public static void iniciarSesion(String cedula) { cedulaActual = cedula; }
    public static String getCedulaActual() { return cedulaActual; }
    public static void cerrar() { cedulaActual = null; }
}
