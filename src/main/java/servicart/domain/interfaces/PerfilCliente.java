package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.PerfilClienteDTOEntrada;
import servicart.domain.dtos.retornos.PerfilClienteDTORetorno;

public interface PerfilCliente {
    PerfilClienteDTORetorno buscarPerfil(PerfilClienteDTOEntrada dto);
}
