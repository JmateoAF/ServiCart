package servicart.ui;

/* Fragmento de código para añadir a CADA controller que tenga un botón de tema.
No es una clase real — es una guía de lo que hay que agregar en cada controller.
Copia estos tres elementos en cada controller:
Campo: @FXML private Button btnTema;
En initialize(): if (btnTema != null) btnTema.setText(ThemeManager.iconoActual());
Método handler:
@FXML
private void onToggleTema(ActionEvent event) {
   ThemeManager.toggle();
   if (btnTema != null) btnTema.setText(ThemeManager.iconoActual());
} */

public final class ThemeToggleMixin {
    private ThemeToggleMixin() {}
}
