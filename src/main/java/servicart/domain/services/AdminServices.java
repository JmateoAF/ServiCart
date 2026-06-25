package servicart.domain.services;

import servicart.data.interfaces.AdminDAO;
import servicart.data.sql.AdminSQLiteDAO;
import servicart.domain.interfaces.LoginAdmin;
import servicart.models.entidades.Administrador;

import java.util.Optional;

public class AdminServices implements LoginAdmin{
    private final AdminDAO<Administrador> adminDAO;

    public AdminServices() { this.adminDAO = new AdminSQLiteDAO(); }

    @Override
    public boolean validarLogin(String nombre, String contrasena) {
        if(nombre == null || nombre.isEmpty() || contrasena == null || contrasena.isEmpty()) {
            return false;
        }

        // Llamamos a la base de datos
        Optional<Administrador> admin = adminDAO.credenciales(nombre, contrasena);

        // Si me devolvió un objeto, el admin existe y la contraseña es correcta
        return admin.isPresent();
    }
}
