package servicart.domain.services;

import servicart.domain.models.entidades.Cliente;
import servicart.domain.interfaces.CrudDAO;

public class ClienteServices {
    // Aquí hacemos la inyección de dependencias, agregación
    private final CrudDAO<Cliente> clienteDAO;

    public ClienteServices(CrudDAO<Cliente> clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void guardarCliente(Cliente cliente) {
        clienteDAO.save(cliente);
    }
}
