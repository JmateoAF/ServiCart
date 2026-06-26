package servicart.data.binary;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entities.Cliente;

import java.util.List;

public class ClienteBinarioDAO extends GenericBinarioDAO<Cliente> implements CrudDAO<Cliente> {

    public ClienteBinarioDAO() {
        super("bin/clientes.bin");
    }

    @Override
    protected String getId(Cliente cliente) {
        return cliente.getCedula();          // identificador natural
    }

    @Override
    protected boolean isActivo(Cliente cliente) {
        return cliente.getActivo() == 1;     // borrado lógico
    }

    /**
     * Borrado lógico: marca el campo activo a 0 en lugar de eliminar físicamente.
     * Si ya estaba inactivo, no hace nada.
     */
    @Override
    public void delete(String cedula) {
        List<Cliente> lista = (cache != null) ? cache : leerTodos();
        for (Cliente c : lista) {
            if (getId(c).equals(cedula)) {
                if (!isActivo(c)) {
                    cache = lista;
                    return;
                }
                c.setActivo(0);
                guardarTodos(lista);
                cache = lista;
                return;
            }
        }
        throw new RuntimeException("Cliente con cédula " + cedula + " no encontrado");
    }
}