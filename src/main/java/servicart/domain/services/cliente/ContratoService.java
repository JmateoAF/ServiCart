package servicart.domain.services.cliente;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.ServicioCatalogo;
import servicart.entities.Cliente;
import servicart.entities.Contrato;
import servicart.entities.enums.CausaTerminacion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        Contrato contrato = new Contrato(LocalDateTime.now(), null, CausaTerminacion.ACTIVO, servicio, cliente);
        contratoDAO.save(contrato);

        return contrato;
    }

    //Termina el contrato por la causa indicada
    public void terminarContrato(Contrato contrato, CausaTerminacion causa) {
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
    public Optional<Contrato> buscarPorId(String id) {
        return contratoDAO.findId(id);
    }
}
