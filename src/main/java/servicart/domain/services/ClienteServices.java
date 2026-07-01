package servicart.domain.services;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.mappers.ClienteMapper;
import servicart.dtos.ClienteResponseDTO;
import servicart.models.entities.Cliente;
import servicart.exceptions.EntidadNoEncontradaException;
import java.util.List;

public class ClienteServices {
    private final ClienteMapper mapper = new ClienteMapper();

    // Metodo privado que obtiene el DAO ya configurado (global)
    private CrudDAO<Cliente> getClienteDAO() {
        return FactoryDAO.getDAO(Cliente.class);
    }

    public void guardarCliente(Cliente cliente) {
        getClienteDAO().save(cliente);
    }

    public ClienteResponseDTO buscarId(String cedula) {
        return getClienteDAO().findId(cedula)
                .map(mapper::aDTO)
                .orElse(null);
    }

    public List<Cliente> buscarTodos() {
        return getClienteDAO().findAll();
    }

    public void actualizar(Cliente cliente) {
        getClienteDAO().update(cliente);
    }

    public void eliminar(String cedula) {
        CrudDAO<Cliente> dao = getClienteDAO();
        dao.findId(cedula).orElseThrow(() -> new EntidadNoEncontradaException(cedula));
        dao.delete(cedula);
    }
}