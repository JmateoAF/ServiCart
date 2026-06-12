package servicart.data.bin;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entidades.Cliente;

import java.util.List;

public class ClienteBinarioDAO extends GenericBinarioDAO<Cliente> implements CrudDAO<Cliente> {

    public ClienteBinarioDAO() {
        super("clientes.bin");               // archivo único para esta entidad
    }

    @Override
    protected String getId(Cliente entidad) {
        return entidad.getCedula();          // identificador natural
    }

    @Override
    protected boolean isActivo(Cliente entidad) {
        return entidad.getActivo() == 1;     // borrado lógico: activo = 1
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
                if (!isActivo(c)) {          // ya eliminado
                    cache = lista;
                    return;
                }
                c.setActivo(0);             // marcado lógico
                guardarTodos(lista);
                cache = lista;
                return;
            }
        }
        throw new RuntimeException("Cliente con cédula " + cedula + " no encontrado");
    }
}