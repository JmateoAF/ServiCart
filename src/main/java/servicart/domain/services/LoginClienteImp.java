package servicart.domain.services;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.LoginDTOEntrada;
import servicart.domain.dtos.LoginDTOSalida;
import servicart.domain.interfaces.LoginCliente;
import servicart.domain.mappers.LoginMapperDomain;
import servicart.entities.Cliente;

import java.util.Optional;

public class LoginClienteImp implements LoginCliente {
    @Override
    public LoginDTOSalida validarLoginCliente(LoginDTOEntrada dto) {
        CrudDAO<Cliente> clienteDAO = FactoryDAO.getDAO(Cliente.class);

        assert clienteDAO != null;
        Optional<Cliente> cliente = clienteDAO.findId(dto.cedula());
        return cliente.map(value -> LoginMapperDomain.entidadADTO(value, dto.baseDatos())).orElse(null);

    }
}