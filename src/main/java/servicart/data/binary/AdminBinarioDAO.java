package servicart.data.binary;

import servicart.data.interfaces.AdminDAO;
import servicart.entities.Administrador;

import java.util.Optional;

public class AdminBinarioDAO extends GenericBinarioDAO<Administrador> implements AdminDAO<Administrador> {
    public AdminBinarioDAO() { super("bin/administrador.bin"); } // archivo único para esta entidad

    @Override
    protected String getId(Administrador administrador) { return administrador.getUsuario(); }

    @Override
    public Optional<Administrador> credenciales(String usuario, String contrasenia) {
        return findId(usuario).filter(admin -> admin.getContrasenia().equals(contrasenia));
    }
}
