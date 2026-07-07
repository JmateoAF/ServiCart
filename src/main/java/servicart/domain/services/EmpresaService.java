package servicart.domain.services;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.interfaces.Observador;
import servicart.entities.Empresa;

import java.util.List;
import java.util.Optional;

public class EmpresaService {
    private final CrudDAO<Empresa> empresaDAO;
    //private final List<Observador> observadoresEstandar;

    public EmpresaService(CrudDAO<Empresa> empresaDAO) {
        this.empresaDAO = empresaDAO;
        //this.observadoresEstandar = observadoresEstandar;
    }

    public Optional<Empresa> buscarPorNombre(String nombre) {
        return empresaDAO.findAll().stream()
                .filter(e -> e.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .map(this::conObservadoresSuscritos);
    }

    public Optional<Empresa> buscarPorId(int id) {
        return empresaDAO.findAll().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .map(this::conObservadoresSuscritos);
    }

    public List<Empresa> listarTodas() {
        return empresaDAO.findAll().stream().map(this::conObservadoresSuscritos).toList();
    }

    private Empresa conObservadoresSuscritos(Empresa empresa) {
        //observadoresEstandar.forEach(empresa::agregarObservador);
        return empresa;
    }
}