package servicart.data.binary;

import servicart.models.catalog.ServicioCatalogo;

/* Se eliminó inicializarDatos() que existía antes
Poblar el catálogo con los 4 servicios iniciales (Agua, Luz, Basura, Internet)
es responsabilidad del CatalogoSeeder en la capa de arranque (servicart.data),
no del DAO, un DAO no decide qué datos existen; solo los guarda y recupera */
public class ServicioCatalogoBinarioDAO extends GenericBinarioDAO<ServicioCatalogo> {
    public ServicioCatalogoBinarioDAO() { super("bin/servicioCatalogo.bin"); }

    @Override
    protected String getId(ServicioCatalogo entidad) { return String.valueOf(entidad.getId()); }
}