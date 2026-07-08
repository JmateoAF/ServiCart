package servicart.domain.services.admin;

import servicart.data.binary.AdminBinarioDAO;
import servicart.data.interfaces.AdminDAO;
import servicart.data.sqlite.AdminSQLiteDAO;
import servicart.domain.dtos.entradas.LoginAdminDTOEntrada;
import servicart.domain.dtos.retornos.LoginAdminDTORetorno;
import servicart.domain.interfaces.LoginAdmin;
import servicart.domain.mappers.LoginAdminMapperDomain;
import servicart.entities.Admin;

import java.util.Optional;

public class LoginAdminImp implements LoginAdmin{
    @Override
    public LoginAdminDTORetorno validarLoginAdmin(LoginAdminDTOEntrada dto) {
        AdminDAO<Admin> adminSQLiteDAO = new AdminSQLiteDAO();
        AdminDAO<Admin> adminBinarioDAO = new AdminBinarioDAO();

        Optional<Admin> adminSQLite = adminSQLiteDAO.credenciales(dto.usuario(), dto.contrasenia());
        Optional<Admin> adminBInario = adminBinarioDAO.credenciales(dto.usuario(), dto.contrasenia());

        if(adminSQLite.isPresent() && adminBInario.isPresent()) return LoginAdminMapperDomain.entidadADTO(dto.usuario(), dto.contrasenia());

        return null;
    }
}
