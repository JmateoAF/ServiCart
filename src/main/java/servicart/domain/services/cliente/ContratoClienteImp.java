package servicart.domain.services.cliente;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.CancelarContratoDTOEntrada;
import servicart.domain.dtos.entradas.ContratoDTOEntrada;
import servicart.domain.dtos.salidas.ContratoDTOSalida;
import servicart.domain.interfaces.ContratoCliente;
import servicart.domain.mappers.ContratoMapperDomain;
import servicart.domain.services.ContratoService;
import servicart.entities.Contrato;
import servicart.entities.enums.CausaTerminacion;

import java.util.List;
public class ContratoClienteImp implements ContratoCliente {

    @Override
    public List<ContratoDTOSalida> listarContratos(ContratoDTOEntrada dto) {
        CrudDAO<Contrato> contratoDAO = FactoryDAO.getDAO(Contrato.class);
        ContratoService contratoService = new ContratoService(contratoDAO);

        return contratoService.buscarPorCliente(dto.cedula()).stream()
                .map(ContratoMapperDomain::entidadADTO)
                .toList();
    }

    @Override
    public void cancelarContrato(CancelarContratoDTOEntrada dto) {
        CrudDAO<Contrato> contratoDAO = FactoryDAO.getDAO(Contrato.class);
        ContratoService contratoService = new ContratoService(contratoDAO);

        Contrato contrato = contratoService.buscarPorId(String.valueOf(dto.idContrato()))
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado: " + dto.idContrato()));

        contratoService.terminarContrato(contrato, CausaTerminacion.CLIENTE);
    }
}