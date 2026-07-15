package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.AgregarAbonoDTOEntrada;
import servicart.domain.dtos.entradas.PanelClienteDTOEntrada;
import servicart.domain.dtos.retornos.ServicioContratadoDTORetorno;

import java.util.List;

public interface PanelCliente {
    List<ServicioContratadoDTORetorno> listarServiciosContratados(PanelClienteDTOEntrada dto);

    void agregarAbonoAlCarrito(AgregarAbonoDTOEntrada dto);
}