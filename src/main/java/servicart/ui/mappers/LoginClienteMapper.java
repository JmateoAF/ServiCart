package servicart.ui.mappers;

import servicart.ui.viewmodels.cliente.LoginClienteInputModel;

public class LoginClienteMapper {
    public static String toCedula(LoginClienteInputModel input) {
        return input.getCedula().trim();
    }

    public static String toBaseDatos(LoginClienteInputModel input) {
        return input.getBaseDatos();
    }
}