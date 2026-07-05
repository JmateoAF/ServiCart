package servicart.domain.services.cliente;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.PerfilClienteDTOEntrada;
import servicart.domain.dtos.salidas.PerfilClienteDTOSalida;
import servicart.domain.interfaces.PerfilCliente;
import servicart.domain.mappers.ClienteMapperDomain;
import servicart.entities.Cliente;
import java.util.Optional;

public class PerfilClienteImp implements PerfilCliente {
    @Override
    public PerfilClienteDTOSalida buscarPerfil(PerfilClienteDTOEntrada dto) {
        CrudDAO<Cliente> clienteDAO = FactoryDAO.getDAO(Cliente.class);
        assert clienteDAO != null;
        Optional<Cliente> cliente = clienteDAO.findId(dto.cedula());
        return cliente.map(ClienteMapperDomain::entidadAPerfilDTO).orElse(null);
    }
}