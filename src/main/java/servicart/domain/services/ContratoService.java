package servicart.domain.services;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.catalog.ServicioCatalogo;
import servicart.domain.models.entities.Cliente;
import servicart.domain.models.entities.Contrato;
import servicart.domain.models.enums.CausaTerminacion;
import servicart.exceptions.ServiCartException;
import java.time.LocalDateTime;
import java.util.List;

/* El contrato se podrá dar por terminado por dos razones:
por solicitud del cliente,
por parte de la empresa, en caso de que el cliente haya excedido por un tiempo prolongado la fecha de corte */

public class ContratoService {
    private final CrudDAO<Contrato> contratoDAO;

    public ContratoService(CrudDAO<Contrato> contratoDAO) {
        this.contratoDAO = contratoDAO;
    }

    //Crea un nuevo contrato activo entre el cliente y el servicio
    public Contrato crearContrato(Cliente cliente, ServicioCatalogo servicio) {
        if (cliente == null)  throw new ServiCartException("El cliente no puede ser nulo");
        if (servicio == null) throw new ServiCartException("El servicio no puede ser nulo");

        Contrato contrato = new Contrato(LocalDateTime.now(), null, CausaTerminacion.ACTIVO, servicio, cliente);
        contratoDAO.save(contrato);

        return contrato;
    }

    //Termina el contrato por la causa indicada
    public void terminarContrato(Contrato contrato, CausaTerminacion causa) {
        if (causa == CausaTerminacion.ACTIVO) throw new ServiCartException("La causa de terminación no puede ser ACTIVO");
        if (!contrato.estaActivo()) throw new ServiCartException("El contrato ya está terminado");

        contrato.setCausaTerminacion(causa);
        contrato.setFechaFin(java.time.LocalDateTime.now());
        contratoDAO.update(contrato);
    }

    public List<Contrato> buscarPorCliente(String cedula) {
        return contratoDAO.findAll().stream().filter(c -> c.getCliente().getCedula().equals(cedula)).toList();
    }

    public List<Contrato> buscarActivos() {
        return contratoDAO.findAll(); // isActivo ya filtra los terminados en el DAO
    }
}
