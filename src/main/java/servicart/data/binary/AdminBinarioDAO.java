package servicart.data.binary;

import servicart.data.interfaces.AdminDAO;
import servicart.entities.Admin;

import java.util.Optional;

public class AdminBinarioDAO extends GenericBinarioDAO<Admin> implements AdminDAO<Admin> {
    public AdminBinarioDAO() { super("bin/administradores.bin"); } // archivo único para esta entidad

    @Override
    protected String getId(Admin admin) { return admin.getUsuario(); }

    @Override
    public Optional<Admin> credenciales(String usuario, String contrasenia) {
        return findId(usuario).filter(admin -> admin.getContrasenia().equals(contrasenia));
    }
}
