package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.CancelarContratoDTOEntrada;
import servicart.domain.dtos.entradas.ContratoDTOEntrada;
import servicart.domain.dtos.retornos.ContratoDTORetorno;

import java.util.List;

public interface ContratoCliente {
    List<ContratoDTORetorno> listarContratos(ContratoDTOEntrada dto);

    void cancelarContrato(CancelarContratoDTOEntrada dto);
}