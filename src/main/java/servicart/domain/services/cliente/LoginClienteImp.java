package servicart.domain.services.cliente;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.LoginClienteDTOEntrada;
import servicart.domain.dtos.salidas.LoginClienteDTOSalida;
import servicart.domain.interfaces.LoginCliente;
import servicart.domain.mappers.LoginClienteMapperDomain;
import servicart.entities.Cliente;

import java.util.Optional;

public class LoginClienteImp implements LoginCliente {
    @Override
    public LoginClienteDTOSalida validarLoginCliente(LoginClienteDTOEntrada dto) {
        CrudDAO<Cliente> clienteDAO = FactoryDAO.getDAO(Cliente.class);

        assert clienteDAO != null;
        Optional<Cliente> cliente = clienteDAO.findId(dto.cedula());
        return cliente.map(value -> LoginClienteMapperDomain.entidadADTO(value, dto.baseDatos(), value.getActivo())).orElse(null);
    }
}