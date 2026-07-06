package servicart.domain.services.cliente;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.CancelarContratoDTOEntrada;
import servicart.domain.dtos.entradas.ContratoDTOEntrada;
import servicart.domain.dtos.salidas.ContratoDTOSalida;
import servicart.domain.interfaces.ContratoCliente;
import servicart.domain.mappers.ContratoMapperDomain;
import servicart.entities.Contrato;
import servicart.entities.enums.CausaTerminacion;

import java.time.LocalDateTime;
import java.util.List;

public class ContratoClienteImp implements ContratoCliente {

    @Override
    public List<ContratoDTOSalida> listarContratos(ContratoDTOEntrada dto) {
        CrudDAO<Contrato> contratoDAO = FactoryDAO.getDAO(Contrato.class);
        assert contratoDAO != null;

        return contratoDAO.findAll().stream()
                .filter(c -> c.getCliente().getCedula().equals(dto.cedula()))
                .map(ContratoMapperDomain::entidadADTO)
                .toList();
    }

    @Override
    public void cancelarContrato(CancelarContratoDTOEntrada dto) {
        CrudDAO<Contrato> contratoDAO = FactoryDAO.getDAO(Contrato.class);
        assert contratoDAO != null;

        Contrato contrato = contratoDAO.findId(String.valueOf(dto.idContrato()))
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado: " + dto.idContrato()));

        contrato.setCausaTerminacion(CausaTerminacion.CLIENTE);
        contrato.setFechaFin(LocalDateTime.now());
        contratoDAO.update(contrato);
    }
}