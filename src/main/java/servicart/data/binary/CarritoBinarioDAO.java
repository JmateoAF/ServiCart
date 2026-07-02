package servicart.data.binary;

import servicart.entities.Carrito;

public class CarritoBinarioDAO extends GenericBinarioDAO<Carrito> {
    public CarritoBinarioDAO() { super("bin/carrito.bin"); } //Archivo único para esta entidad

    @Override
    protected String getId(Carrito carrito) { return String.valueOf(carrito.getId()); } //Identificador natural
}