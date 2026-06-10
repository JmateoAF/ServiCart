package servicart.domain.services;

import servicart.domain.models.entidades.Cliente;
import servicart.domain.interfaces.CrudDAO;

import java.util.List;
import java.util.Optional;

public class ClienteServices {
    // Aquí hacemos la inyección de dependencias, agregación
    private final CrudDAO<Cliente> clienteDAO;

    public ClienteServices(CrudDAO<Cliente> clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void guardarCliente(Cliente cliente) {
        clienteDAO.save(cliente);
    }

    public Optional<Cliente> buscarId(String cedula){
        return clienteDAO.findId(cedula);
    }

    public List<Cliente> buscarTodos(){
        return clienteDAO.findAll();
    }

    public void actualizar(Cliente cliente){
        clienteDAO.update(cliente);
    }

    public void eliminar(String  cedula){
        clienteDAO.delete(cedula);
    }
}
