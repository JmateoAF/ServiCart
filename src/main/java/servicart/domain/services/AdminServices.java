package servicart.domain.services;

import servicart.data.interfaces.AdminDAO;
import servicart.domain.interfaces.LoginAdmin;
import servicart.models.entities.Administrador;

public class AdminServices implements LoginAdmin{
    private final AdminDAO<Administrador> adminDAO;

    public AdminServices(AdminDAO<Administrador> adminDAO) { this.adminDAO = adminDAO; }

    @Override
    public boolean validarLogin(String usuario, String contrasena) {
        if(usuario == null || usuario.isBlank()) return false;
        if(contrasena == null || contrasena.isBlank()) return false;

        return adminDAO.credenciales(usuario, contrasena).isPresent();
    }
}
