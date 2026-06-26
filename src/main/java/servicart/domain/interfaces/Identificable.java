package servicart.domain.interfaces;

/* Contrato para entidades con ID numérico autoincremental.
GenericBinarioDAO lo usa para asignar el ID al guardar sin
necesitar conocer el tipo concreto de la entidad */

public interface Identificable {
    int getId();
    void setId(int id);
}