package servicart.domain.services.admin;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.ActualizarTarifaDTOEntrada;
import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;
import servicart.domain.interfaces.AdminTarifas;
import servicart.domain.mappers.AdminTarifasMapperDomain;
import servicart.entities.ServicioCatalogo;
import servicart.entities.enums.TipoValorFactura;

import java.util.List;

public class AdminTarifasImp implements AdminTarifas {

    @Override
    public List<TarifaDetalleDTORetorno> listarTarifas() {
        return FactoryDAO.getDAO(ServicioCatalogo.class).findAll().stream()
                .map(AdminTarifasMapperDomain::entidadADTO)
                .toList();
    }

    @Override
    public void actualizarTarifa(ActualizarTarifaDTOEntrada dto) {
        CrudDAO<ServicioCatalogo> servicioDAO = FactoryDAO.getDAO(ServicioCatalogo.class);

        ServicioCatalogo servicio = servicioDAO.findId(String.valueOf(dto.idServicio()))
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + dto.idServicio()));

        // Solo uno de los dos aplica según el TipoValorFactura del servicio (ver CalculoFijo/CalculoVariable);
        // no se toca el otro para no dejar basura en un campo que ese servicio nunca usa
        if (servicio.getTipoValor() == TipoValorFactura.FIJO) servicio.setTarifaFija(dto.tarifaBase());
        else servicio.setTarifaPorUnidad(dto.tarifaBase());

        servicio.setTasaInteresDiario(dto.tasaInteresDiarioPorcentaje() / 100);
        servicio.setCostoReactivacion(dto.costoReactivacion());

        servicioDAO.update(servicio);
    }
}
