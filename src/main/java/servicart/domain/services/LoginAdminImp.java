package servicart.domain.services;

import servicart.data.interfaces.AdminDAO;
import servicart.domain.interfaces.LoginAdmin;
import servicart.entities.Administrador;

public class LoginAdminImp implements LoginAdmin{
    private final AdminDAO<Administrador> adminDAO;

    public LoginAdminImp(AdminDAO<Administrador> adminDAO) { this.adminDAO = adminDAO; }

    @Override
    public boolean validarLogin(String usuario, String contrasena) {
        return adminDAO.credenciales(usuario, contrasena).isPresent();
    }
}
