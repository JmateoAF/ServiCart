package servicart.domain.interfaces;

import servicart.domain.dtos.entradas.CrearEmpresaDTOEntrada;
import servicart.domain.dtos.entradas.CrearServicioDTOEntrada;
import servicart.domain.dtos.retornos.EmpresaDTORetorno;
import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;

import java.util.List;

public interface AdminEmpresas {
    List<EmpresaDTORetorno> listarEmpresas();
    void crearEmpresa(CrearEmpresaDTOEntrada dto);
    List<TarifaDetalleDTORetorno> listarServicios();
    void crearServicio(CrearServicioDTOEntrada dto);
    void eliminarServicio(int idServicio);
}
