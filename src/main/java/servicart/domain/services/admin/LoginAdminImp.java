package servicart.domain.services.admin;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.AdminDAO;
import servicart.domain.dtos.entradas.LoginAdminDTOEntrada;
import servicart.domain.dtos.retornos.LoginAdminDTORetorno;
import servicart.domain.interfaces.LoginAdmin;
import servicart.domain.mappers.LoginAdminMapperDomain;
import servicart.entities.Admin;

import java.util.Optional;

public class LoginAdminImp implements LoginAdmin {
    @Override
    public LoginAdminDTORetorno validarLoginAdmin(LoginAdminDTOEntrada dto) {
        AdminDAO<Admin> adminDAO = FactoryDAO.getAdminDAO();

        Optional<Admin> admin = adminDAO.credenciales(dto.usuario(), dto.contrasenia());

        if (admin.isPresent()) return LoginAdminMapperDomain.entidadADTO(dto.usuario(), dto.contrasenia());

        return null;
    }
}
