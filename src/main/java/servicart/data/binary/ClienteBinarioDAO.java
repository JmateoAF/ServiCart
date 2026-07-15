package servicart.data.binary;

import servicart.data.interfaces.ClienteAdminDAO;
import servicart.entities.Cliente;

import java.util.List;

public class ClienteBinarioDAO extends GenericBinarioDAO<Cliente> implements ClienteAdminDAO<Cliente> {
    public ClienteBinarioDAO() {
        super("bin/clientes.bin");
    }

    @Override
    protected String getId(Cliente cliente) {
        return cliente.getCedula(); //Identificador natural
    }

    @Override
    protected boolean isActivo(Cliente cliente) {
        return cliente.getActivo() == 1; //Borrado lógico
    }

    /* Borrado lógico: marca el campo activo a 0 en lugar de eliminar físicamente
    Si ya estaba inactivo, no hace nada */
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

        throw new RuntimeException(cedula);
    }
}