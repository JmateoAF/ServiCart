package servicart.data.binary;

import servicart.models.entities.CorteServicio;
import servicart.models.enums.EstadoCorte;
import servicart.exceptions.EntidadNoEncontradaException;

import java.util.List;

/* Se eliminaron cortarServicio() y reactivar() que existían antes
Cortar y reactivar son transiciones de estado del dominio -> CorteService:
cortar: corteServicio.setEstadoCorte(CORTADO) -> update()
reactivar: corteServicio.setEstadoCorte(ACTIVO) -> update()
isActivo incluye ACTIVO y CORTADO (ambos son registros vigentes)
Solo TERMINADO se excluye del findAll() normal
delete() -> borrado lógico a TERMINADO (el DAO decide el HOW, el dominio el WHEN) */
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
        return entidad.getEstadoCorte() == EstadoCorte.ACTIVO;
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

        throw new EntidadNoEncontradaException(id);
    }
}


