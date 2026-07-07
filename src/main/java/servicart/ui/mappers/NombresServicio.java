package servicart.ui.mappers;

public class NombresServicio {
    public static String nombreServicio(String tipoServicio) {
        return switch (tipoServicio) {
            case "AGUA" -> "Agua Potable";
            case "LUZ" -> "Electricidad";
            case "BASURA" -> "Recolección de Basura";
            case "INTERNET" -> "Internet";
            default -> tipoServicio;
        };
    }
}