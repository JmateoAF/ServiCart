package servicart.data.binary;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entities.CorteServicio;
import servicart.domain.models.enums.EstadoCorte;

import java.util.List;

public class CorteServicioBinarioDAO extends GenericBinarioDAO<CorteServicio>
        implements CrudDAO<CorteServicio> {

    public CorteServicioBinarioDAO() {
        super("bin/corteServicio.bin");
    }

    @Override
    protected String getId(CorteServicio entidad) {
        return String.valueOf(entidad.getId());
    }

    @Override
    protected boolean isActivo(CorteServicio entidad) {
        return entidad.getEstadoCorte() == EstadoCorte.ACTIVO;
    }

    /**
     * Borrado lógico: cambia el estado a TERMINADO en lugar de eliminar.
     * Si ya está TERMINADO, no se regraba el archivo.
     */
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
        throw new RuntimeException("CorteServicio con ID " + id + " no encontrado");
    }

    public void cortarServicio(String id) {
        List<CorteServicio> lista = (cache != null) ? cache : leerTodos();
        for (CorteServicio c : lista) {
            if (getId(c).equals(id)) {
                if (!isActivo(c)) {
                    cache = lista;
                    return;
                }
                c.setEstadoCorte(EstadoCorte.CORTADO);
                guardarTodos(lista);
                cache = lista;
                return;
            }
        }
        throw new RuntimeException("CorteServicio con ID " + id + " no encontrado");
    }
    public void reactivar(String id) {
        List<CorteServicio> lista = (cache != null) ? cache : leerTodos();
        for (CorteServicio c : lista) {
            if (getId(c).equals(id)) {
                if (isActivo(c)) {
                    cache = lista;
                    return;
                }
                c.setEstadoCorte(EstadoCorte.ACTIVO);
                guardarTodos(lista);
                cache = lista;
                return;
            }
        }
        throw new RuntimeException("CorteServicio con ID " + id + " no encontrado");
    }
}


