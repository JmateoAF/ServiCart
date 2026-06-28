package servicart.ui.core;

import javafx.scene.Scene;
import servicart.exceptions.ServiCartException;

import java.net.URL;

/* Gestor del tema visual (oscuro / claro).
Funciona intercambiando la hoja de estilos de la Scene.
Como Navegador usa setRoot() (no crea una nueva Scene),
los estilos persisten automáticamente entre cambios de vista.
Uso desde Main: ThemeManager.inicializar(stage.getScene());
Uso desde cualquier controller: ThemeManager.toggle();
btnTema.setText(ThemeManager.iconoActual()); */

public class ManejadorTema {
    private static final String oscuroCSS = "assets/css/modoOscuro.css";
    private static final String claroCSS = "assets/css/modoClaro.css";
    private static boolean dark = true;
    private static Scene scene;

    //Llamado una sola vez desde el Main, con el tema oscuro por defecto
    public static void inicializar(Scene s) {
        scene = s;
        aplicarTema();
    }

    //Alterna entre oscuro y claro, llamado desde el botón de toggle
    public static void toggle() {
        dark = !dark;
        aplicarTema();
    }

    public static boolean isDark() { return dark; }

    public static String iconoActual() { return dark ? "claro" : "oscuro"; }

    private static void aplicarTema() {
        if (scene == null) return;
        scene.getStylesheets().clear();
        String ruta = dark ? oscuroCSS : claroCSS;
        URL url = ManejadorTema.class.getClassLoader().getResource(ruta);
        if (url == null) throw new ServiCartException("Hoja de estilos no encontrada: " + ruta);
        scene.getStylesheets().add(url.toExternalForm());
    }
}
