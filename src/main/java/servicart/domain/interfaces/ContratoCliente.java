package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.CancelarContratoDTOEntrada;
import servicart.domain.dtos.entradas.ContratoDTOEntrada;
import servicart.domain.dtos.salidas.ContratoDTOSalida;

import java.util.List;

public interface ContratoCliente {
    List<ContratoDTOSalida> listarContratos(ContratoDTOEntrada dto);
    void cancelarContrato(CancelarContratoDTOEntrada dto);
}