package servicart.domain.models.entidades;

/*
 *  Se introdujo para que el metodo save de
 * {GenericBinarioDAO} pueda calcular y asignar automáticamente
 * el próximo ID disponible al guardar una nueva entidad, evitando
 * tener que sobreescribir {@code save} en cada DAO concreto.
 * Ya que en una clase genérica con <T> no puede llamar directamente
 * a entidad.setId() porque el compilador no sabe si T tiene ese metodo.
 */
public interface Identificable {
    int getId();
    void setId(int id);
}
