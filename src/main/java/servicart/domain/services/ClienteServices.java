package servicart.domain.services;

import servicart.domain.models.entities.Cliente;
import servicart.data.interfaces.CrudDAO;
import servicart.exceptions.EntidadNoEncontradaException;
import servicart.exceptions.ServiCartException;

import java.util.List;
import java.util.Optional;

public class ClienteServices {
    // Aquí hacemos la inyección de dependencias
    private final CrudDAO<Cliente> clienteDAO;

    public ClienteServices(CrudDAO<Cliente> clienteDAO) { this.clienteDAO = clienteDAO; }

    public void guardarCliente(Cliente cliente) {
        validarCliente(cliente);
        clienteDAO.save(cliente);
    }

    public Optional<Cliente> buscarId(String cedula){
        if(cedula == null || cedula.isBlank())
            throw new ServiCartException("La cédula no puede esta vacía");

        return clienteDAO.findId(cedula);
    }

    public List<Cliente> buscarTodos(){
        return clienteDAO.findAll();
    }

    public void actualizar(Cliente cliente){
        validarCliente(cliente);
        clienteDAO.update(cliente);
    }

    //Eliminación lógica
    public void eliminar(String  cedula) {
        clienteDAO.findId(cedula).orElseThrow(() -> new EntidadNoEncontradaException(cedula));
        clienteDAO.delete(cedula);
    }

    private void validarCliente(Cliente c) {
        if (c == null) throw new ServiCartException("El cliente no puede ser nulo");
        if (c.getCedula() == null || c.getCedula().isBlank()) throw new ServiCartException("La cédula es obligatoria");
        if (c.getNombre() == null || c.getNombre().isBlank()) throw new ServiCartException("El nombre es obligatorio");
        if (c.getEmail() == null || !c.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) throw new ServiCartException("El email no tiene un formato válido");
        if (c.getCelular() == null || !c.getCelular().matches("\\d{10}")) throw new ServiCartException("El celular debe tener 10 dígitos");
    }
}
