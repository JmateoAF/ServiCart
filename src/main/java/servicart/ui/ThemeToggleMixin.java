package servicart.ui;

import javafx.scene.control.Button;

public final class ThemeToggleMixin {
    public static void configurar(Button btnTema) {
        if (btnTema == null) return;
        btnTema.setText(ManejadorTema.iconoActual());
        btnTema.setOnAction(e -> {
            ManejadorTema.toggle();
            btnTema.setText(ManejadorTema.iconoActual());
        });
    }
}

// Uso en cualquier controller:
// ThemeToggleMixin.configurar(btnTema);  // una sola línea en initialize()