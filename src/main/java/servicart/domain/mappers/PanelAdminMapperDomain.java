package servicart.domain.mappers;

import servicart.domain.dtos.retornos.ResumenAdminDTORetorno;
import servicart.domain.dtos.retornos.TarifaDTORetorno;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;
import servicart.entities.Cliente;
import servicart.entities.ServicioCatalogo;
import servicart.entities.enums.TipoValorFactura;

public class PanelAdminMapperDomain {

    public static UsuarioDTORetorno usuarioADTO(Cliente cliente) {
        return new UsuarioDTORetorno(
                cliente.getCedula(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getCelular(),
                cliente.getActivo() == 1);
    }

    public static TarifaDTORetorno tarifaADTO(ServicioCatalogo servicio) {
        double valor = servicio.getTipoValor() == TipoValorFactura.FIJO
                ? servicio.getTarifaFija()
                : servicio.getTarifaPorUnidad();

        return new TarifaDTORetorno(
                servicio.getTipo().name(),
                servicio.getEmpresa().getNombre(),
                String.format("%.2f", valor),
                servicio.getTipoValor().name());
    }

    public static ResumenAdminDTORetorno resumenADTO(int usuariosActivos, int cortados, int conMora, String modoActivo) {
        return new ResumenAdminDTORetorno(usuariosActivos, cortados, conMora, modoActivo);
    }
}