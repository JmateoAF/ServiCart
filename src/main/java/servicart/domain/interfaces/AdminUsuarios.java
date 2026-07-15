package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.CambiarEstadoUsuarioDTOEntrada;
import servicart.domain.dtos.entradas.ContratarServicioDTOEntrada;
import servicart.domain.dtos.entradas.UsuarioDTOEntrada;
import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;

import java.util.List;

public interface AdminUsuarios {
    List<UsuarioDTORetorno> listarUsuarios(String filtro);

    void crearUsuario(UsuarioDTOEntrada dto);

    void editarUsuario(UsuarioDTOEntrada dto);

    void cambiarEstado(CambiarEstadoUsuarioDTOEntrada dto);

    List<TarifaDetalleDTORetorno> listarServiciosDisponibles(String cedula);

    void contratarServicio(ContratarServicioDTOEntrada dto);
}
