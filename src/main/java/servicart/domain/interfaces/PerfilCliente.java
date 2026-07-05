package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.PerfilClienteDTOEntrada;
import servicart.domain.dtos.salidas.PerfilClienteDTOSalida;

public interface PerfilCliente {
    PerfilClienteDTOSalida buscarPerfil(PerfilClienteDTOEntrada dto);
}
