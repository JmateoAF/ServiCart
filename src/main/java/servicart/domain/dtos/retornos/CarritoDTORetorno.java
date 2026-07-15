package servicart.domain.dtos.retornos;

import java.util.List;

public record CarritoDTORetorno(int idCarrito, List<AbonoCarritoDTORetorno> items, double total) {
}