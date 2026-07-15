package servicart.domain.services.empresa;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.Cliente;
import servicart.entities.Contrato;
import servicart.entities.ServicioCatalogo;
import servicart.entities.enums.CausaTerminacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ContratoService {
    private final CrudDAO<Contrato> contratoDAO;

    public ContratoService(CrudDAO<Contrato> contratoDAO) {
        this.contratoDAO = contratoDAO;
    }

    public void crearContrato(Cliente cliente, ServicioCatalogo servicio) {
        Contrato contrato = new Contrato(LocalDateTime.now(), null, CausaTerminacion.ACTIVO, servicio, cliente);
        contratoDAO.save(contrato);
    }

    public void terminarContrato(Contrato contrato, CausaTerminacion causa) {
        contrato.setCausaTerminacion(causa);
        contrato.setFechaFin(LocalDateTime.now());
        contratoDAO.update(contrato);
    }

    public List<Contrato> buscarPorCliente(String cedula) {
        return contratoDAO.findAll().stream().filter(c -> c.getCliente().getCedula().equals(cedula)).toList();
    }

    public List<Contrato> buscarActivos() {
        return contratoDAO.findAll().stream().filter(Contrato::estaActivo).toList();
    }

    public Optional<Contrato> buscarPorId(String id) {
        return contratoDAO.findId(id);
    }
}