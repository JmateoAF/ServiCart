package servicart.data.binary;

import servicart.entities.CorteServicio;
import servicart.entities.enums.EstadoCorte;

import java.util.List;

public class CorteServicioBinarioDAO extends GenericBinarioDAO<CorteServicio> {
    public CorteServicioBinarioDAO() {
        super("bin/corteServicio.bin");
    }

    @Override
    protected String getId(CorteServicio entidad) {
        return String.valueOf(entidad.getId());
    }

    @Override
    protected boolean isActivo(CorteServicio entidad) {
        return entidad.getEstadoCorte() != EstadoCorte.TERMINADO;
    }

    /* Borrado lógico: cambia el estado a TERMINADO en lugar de eliminar.
    Si ya está TERMINADO, no se regraba el archivo */
    @Override
    public void delete(String id) {
        List<CorteServicio> lista = (cache != null) ? cache : leerTodos();
        for (CorteServicio c : lista) {
            if (getId(c).equals(id)) {
                if (c.getEstadoCorte() == EstadoCorte.TERMINADO) {
                    cache = lista;

                    return;
                }

                c.setEstadoCorte(EstadoCorte.TERMINADO);
                guardarTodos(lista);
                cache = lista;

                return;
            }
        }
    }
}


