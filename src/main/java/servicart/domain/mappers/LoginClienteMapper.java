package servicart.domain.mappers;

import servicart.ui.viewmodels.cliente.LoginClienteModel;

public class LoginClienteMapper {
    public static String toCedula(LoginClienteModel input) {
        return input.getCedula().trim();
    }

    public static String toBaseDatos(LoginClienteModel input) {
        return input.getBaseDatos();
    }
}