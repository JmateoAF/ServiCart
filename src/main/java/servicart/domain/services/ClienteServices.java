package servicart.domain.services;

import servicart.domain.mappers.ClienteMapper;
import servicart.dtos.ClienteDTO;
import servicart.models.entities.Cliente;
import servicart.data.interfaces.CrudDAO;
import servicart.exceptions.EntidadNoEncontradaException;
import java.util.List;

public class ClienteServices {
    private final CrudDAO<Cliente> clienteDAO;
    private final ClienteMapper mapper;

    public ClienteServices(CrudDAO<Cliente> clienteDAO) {
        this.clienteDAO = clienteDAO;
        this.mapper = new ClienteMapper();
    }

    public void guardarCliente(Cliente cliente) { clienteDAO.save(cliente); }

    public ClienteDTO buscarId(String cedula){ return clienteDAO.findId(cedula).map(mapper::aDTO).orElse(null);

    public List<Cliente> buscarTodos(){
        return clienteDAO.findAll();
    }

    public void actualizar(Cliente cliente){ clienteDAO.update(cliente); }

    //Eliminación lógica
    public void eliminar(String  cedula) {
        clienteDAO.findId(cedula).orElseThrow(() -> new EntidadNoEncontradaException(cedula));
        clienteDAO.delete(cedula);
    }
}
