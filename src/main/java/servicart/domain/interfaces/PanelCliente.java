package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.AgregarAbonoDTOEntrada;
import servicart.domain.dtos.entradas.PanelClienteDTOEntrada;
import servicart.domain.dtos.salidas.ServicioContratadoDTOSalida;
import java.util.List;

public interface PanelCliente {
    List<ServicioContratadoDTOSalida> listarServiciosContratados(PanelClienteDTOEntrada dto);
    void agregarAbonoAlCarrito(AgregarAbonoDTOEntrada dto);
}