package servicart.domain.mappers;

import servicart.domain.dtos.retornos.AbonoCarritoDTORetorno;
import servicart.domain.dtos.retornos.CarritoDTORetorno;
import servicart.entities.Carrito;

public class CarritoMapperDomain {

    public static CarritoDTORetorno entidadADTO(Carrito carrito) {
        var items = carrito.getAbonos().stream()
                .map(a -> new AbonoCarritoDTORetorno(a.getId(), a.getDescripcionServicio(), a.getMonto(), a.getFactura().getId()))
                .toList();

        return new CarritoDTORetorno(carrito.getId(), items, carrito.getTotal());
    }
}