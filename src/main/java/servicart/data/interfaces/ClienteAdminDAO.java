package servicart.data.interfaces;

import java.util.List;

/* Extiende CrudDAO para exponer también los registros con borrado lógico (activo=0),
invisibles para findAll(). Solo el panel admin necesita verlos, para poder reactivarlos */
public interface ClienteAdminDAO<T> extends CrudDAO<T> {
    List<T> findAllSinFiltro();
}
