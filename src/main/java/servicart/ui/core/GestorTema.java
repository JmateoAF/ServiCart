package servicart.ui.core;

import javafx.scene.control.Button;

public final class GestorTema {
    public static void configurar(Button btnTema) {
        if (btnTema == null) return;

        actualizarTexto(btnTema);

        btnTema.setOnAction(e -> {
            ManejadorTema.toggle();
            actualizarTexto(btnTema);
        });
    }

    private static void actualizarTexto(Button btn) {
        btn.setText(ManejadorTema.isDark() ? "Modo Claro" : "Modo Oscuro");
    }
}