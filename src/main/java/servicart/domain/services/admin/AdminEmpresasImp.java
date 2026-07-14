package servicart.domain.services.admin;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.CrearEmpresaDTOEntrada;
import servicart.domain.dtos.entradas.CrearServicioDTOEntrada;
import servicart.domain.dtos.retornos.EmpresaDTORetorno;
import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;
import servicart.domain.interfaces.AdminEmpresas;
import servicart.domain.mappers.AdminTarifasMapperDomain;
import servicart.entities.Contrato;
import servicart.entities.Empresa;
import servicart.entities.ServicioCatalogo;
import servicart.entities.enums.TipoServicio;
import servicart.entities.enums.TipoValorFactura;

import java.util.List;

public class AdminEmpresasImp implements AdminEmpresas {

    @Override
    public List<EmpresaDTORetorno> listarEmpresas() {
        List<ServicioCatalogo> servicios = FactoryDAO.getDAO(ServicioCatalogo.class).findAll();

        return FactoryDAO.getDAO(Empresa.class).findAll().stream()
                .map(empresa -> new EmpresaDTORetorno(
                        empresa.getId(),
                        empresa.getNombre(),
                        (int) servicios.stream().filter(s -> s.getEmpresa().getId() == empresa.getId()).count()))
                .toList();
    }

    @Override
    public void crearEmpresa(CrearEmpresaDTOEntrada dto) {
        CrudDAO<Empresa> empresaDAO = FactoryDAO.getDAO(Empresa.class);
        String nombre = dto.nombre() == null ? "" : dto.nombre().trim();

        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío");
        }
        boolean yaExiste = empresaDAO.findAll().stream().anyMatch(e -> e.getNombre().equalsIgnoreCase(nombre));
        if (yaExiste) {
            throw new IllegalArgumentException("Ya existe una empresa llamada " + nombre);
        }

        empresaDAO.save(new Empresa(nombre));
    }

    @Override
    public List<TarifaDetalleDTORetorno> listarServicios() {
        return FactoryDAO.getDAO(ServicioCatalogo.class).findAll().stream()
                .map(AdminTarifasMapperDomain::entidadADTO)
                .toList();
    }

    @Override
    public void crearServicio(CrearServicioDTOEntrada dto) {
        CrudDAO<Empresa> empresaDAO = FactoryDAO.getDAO(Empresa.class);
        CrudDAO<ServicioCatalogo> servicioDAO = FactoryDAO.getDAO(ServicioCatalogo.class);

        Empresa empresa = empresaDAO.findId(String.valueOf(dto.idEmpresa()))
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada: " + dto.idEmpresa()));

        TipoServicio tipoServicio;
        TipoValorFactura tipoValor;
        try {
            tipoServicio = TipoServicio.valueOf(dto.tipoServicio());
            tipoValor = TipoValorFactura.valueOf(dto.tipoValor());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new IllegalArgumentException("Selecciona el tipo de servicio y el tipo de tarifa");
        }

        boolean yaExiste = servicioDAO.findAll().stream()
                .anyMatch(s -> s.getEmpresa().getId() == empresa.getId() && s.getTipo() == tipoServicio);
        if (yaExiste) {
            throw new IllegalArgumentException(empresa.getNombre() + " ya tiene registrado el servicio " + tipoServicio.name());
        }

        if (dto.tarifaBase() < 0 || dto.tasaInteresDiarioPorcentaje() < 0 || dto.costoReactivacion() < 0) {
            throw new IllegalArgumentException("Los valores no pueden ser negativos");
        }
        if (dto.diasParaCorte() <= 0) {
            throw new IllegalArgumentException("Los días para corte deben ser mayores a 0");
        }

        ServicioCatalogo servicio = new ServicioCatalogo(
                empresa, tipoServicio, tipoValor, dto.costoReactivacion(), dto.tasaInteresDiarioPorcentaje() / 100, dto.diasParaCorte());

        if (tipoValor == TipoValorFactura.FIJO) servicio.setTarifaFija(dto.tarifaBase());
        else servicio.setTarifaPorUnidad(dto.tarifaBase());

        servicioDAO.save(servicio);
    }

    @Override
    public void eliminarServicio(int idServicio) {
        CrudDAO<ServicioCatalogo> servicioDAO = FactoryDAO.getDAO(ServicioCatalogo.class);

        servicioDAO.findId(String.valueOf(idServicio))
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + idServicio));

        boolean enUso = FactoryDAO.getDAO(Contrato.class).findAll().stream()
                .anyMatch(c -> c.getServicio().getId() == idServicio);
        if (enUso) {
            throw new IllegalStateException("No se puede quitar: hay contratos activos usando este servicio");
        }

        servicioDAO.delete(String.valueOf(idServicio));
    }
}
