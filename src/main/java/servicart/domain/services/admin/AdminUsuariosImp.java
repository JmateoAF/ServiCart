package servicart.domain.services.admin;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.ClienteAdminDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.dtos.entradas.CambiarEstadoUsuarioDTOEntrada;
import servicart.domain.dtos.entradas.ContratarServicioDTOEntrada;
import servicart.domain.dtos.entradas.UsuarioDTOEntrada;
import servicart.domain.dtos.retornos.TarifaDetalleDTORetorno;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;
import servicart.domain.interfaces.AdminUsuarios;
import servicart.domain.mappers.AdminTarifasMapperDomain;
import servicart.domain.mappers.PanelAdminMapperDomain;
import servicart.domain.services.empresa.ContratoService;
import servicart.entities.Cliente;
import servicart.entities.Contrato;
import servicart.entities.ServicioCatalogo;

import java.util.List;
import java.util.Objects;

public class AdminUsuariosImp implements AdminUsuarios {

    @Override
    public List<UsuarioDTORetorno> listarUsuarios(String filtro) {
        ClienteAdminDAO<Cliente> clienteDAO = FactoryDAO.getClienteAdminDAO();
        String termino = filtro == null ? "" : filtro.trim().toLowerCase();

        return clienteDAO.findAllSinFiltro().stream()
                .filter(c -> coincide(c, termino))
                .map(PanelAdminMapperDomain::usuarioADTO)
                .toList();
    }

    private boolean coincide(Cliente cliente, String termino) {
        if (termino.isEmpty()) return true;
        return cliente.getCedula().toLowerCase().contains(termino)
                || cliente.getNombre().toLowerCase().contains(termino)
                || cliente.getEmail().toLowerCase().contains(termino);
    }

    @Override
    public void crearUsuario(UsuarioDTOEntrada dto) {
        ClienteAdminDAO<Cliente> clienteDAO = FactoryDAO.getClienteAdminDAO();

        if (clienteDAO.findId(dto.cedula()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con la cédula " + dto.cedula());
        }

        List<Cliente> existentes = clienteDAO.findAllSinFiltro();
        if (existentes.stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(dto.email()))) {
            throw new IllegalArgumentException("Ya existe un usuario con el email " + dto.email());
        }
        if (existentes.stream().anyMatch(c -> c.getCelular().equals(dto.celular()))) {
            throw new IllegalArgumentException("Ya existe un usuario con el celular " + dto.celular());
        }

        clienteDAO.save(new Cliente(dto.cedula(), dto.nombre(), dto.email(), dto.celular(), 1));
    }

    @Override
    public void editarUsuario(UsuarioDTOEntrada dto) {
        ClienteAdminDAO<Cliente> clienteDAO = FactoryDAO.getClienteAdminDAO();

        Cliente cliente = clienteDAO.findId(dto.cedula())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.cedula()));

        List<Cliente> otros = clienteDAO.findAllSinFiltro().stream()
                .filter(c -> !c.getCedula().equals(dto.cedula()))
                .toList();
        if (otros.stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(dto.email()))) {
            throw new IllegalArgumentException("Ya existe un usuario con el email " + dto.email());
        }
        if (otros.stream().anyMatch(c -> c.getCelular().equals(dto.celular()))) {
            throw new IllegalArgumentException("Ya existe un usuario con el celular " + dto.celular());
        }

        cliente.setNombre(dto.nombre());
        cliente.setEmail(dto.email());
        cliente.setCelular(dto.celular());
        clienteDAO.update(cliente);
    }

    @Override
    public void cambiarEstado(CambiarEstadoUsuarioDTOEntrada dto) {
        ClienteAdminDAO<Cliente> clienteDAO = FactoryDAO.getClienteAdminDAO();

        Cliente cliente = clienteDAO.findId(dto.cedula())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.cedula()));

        cliente.setActivo(dto.activo() ? 1 : 0);
        clienteDAO.update(cliente);
    }

    @Override
    public List<TarifaDetalleDTORetorno> listarServiciosDisponibles(String cedula) {
        ContratoService contratoService = new ContratoService(FactoryDAO.getDAO(Contrato.class));

        List<Integer> idsYaContratados = contratoService.buscarPorCliente(cedula).stream()
                .filter(Contrato::estaActivo)
                .map(c -> c.getServicio().getId())
                .toList();

        return Objects.requireNonNull(FactoryDAO.getDAO(ServicioCatalogo.class)).findAll().stream()
                .filter(s -> !idsYaContratados.contains(s.getId()))
                .map(AdminTarifasMapperDomain::entidadADTO)
                .toList();
    }

    @Override
    public void contratarServicio(ContratarServicioDTOEntrada dto) {
        ClienteAdminDAO<Cliente> clienteDAO = FactoryDAO.getClienteAdminDAO();
        CrudDAO<ServicioCatalogo> servicioDAO = FactoryDAO.getDAO(ServicioCatalogo.class);
        ContratoService contratoService = new ContratoService(FactoryDAO.getDAO(Contrato.class));

        Cliente cliente = clienteDAO.findId(dto.cedula())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.cedula()));

        assert servicioDAO != null;
        ServicioCatalogo servicio = servicioDAO.findId(String.valueOf(dto.idServicio()))
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + dto.idServicio()));

        boolean yaContratado = contratoService.buscarPorCliente(dto.cedula()).stream()
                .filter(Contrato::estaActivo)
                .anyMatch(c -> c.getServicio().getId() == servicio.getId());
        if (yaContratado) {
            throw new IllegalArgumentException(cliente.getNombre() + " ya tiene contratado este servicio");
        }

        contratoService.crearContrato(cliente, servicio);
    }
}
