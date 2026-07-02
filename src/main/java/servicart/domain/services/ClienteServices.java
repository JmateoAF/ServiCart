package servicart.domain.services;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.mappers.ClienteMapper;
import servicart.ui.dtos.ClienteRequestDTO;
import servicart.ui.dtos.ClienteResponseDTO;
import servicart.entities.Cliente;
import servicart.exceptions.EntidadNoEncontradaException;
import java.util.List;

public class ClienteServices {
    private final ClienteMapper mapper = new ClienteMapper();

    // Metodo privado que obtiene el DAO ya configurado (global)
    private CrudDAO<Cliente> getClienteDAO() {
        return FactoryDAO.getDAO(Cliente.class);
    }

    public void guardarCliente(ClienteRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }
        Cliente cliente = new Cliente(
                dto.getCedula(),
                dto.getNombre(),
                dto.getEmail(),
                dto.getCelular()
        );
        cliente.setActivo(dto.getActivo());
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

    public void actualizarCliente(ClienteRequestDTO dto) {
        Cliente cliente = getClienteDAO().findId(dto.getCedula())
                .orElseThrow(() -> new EntidadNoEncontradaException(dto.getCedula()));
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setCelular(dto.getCelular());
        cliente.setActivo(dto.getActivo());
        getClienteDAO().update(cliente);
    }

    public void eliminar(String cedula) {
        CrudDAO<Cliente> dao = getClienteDAO();
        dao.findId(cedula).orElseThrow(() -> new EntidadNoEncontradaException(cedula));
        dao.delete(cedula);
    }
}