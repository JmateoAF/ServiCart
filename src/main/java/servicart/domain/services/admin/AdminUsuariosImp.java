package servicart.domain.services.admin;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.ClienteAdminDAO;
import servicart.domain.dtos.entradas.CambiarEstadoUsuarioDTOEntrada;
import servicart.domain.dtos.entradas.UsuarioDTOEntrada;
import servicart.domain.dtos.retornos.UsuarioDTORetorno;
import servicart.domain.interfaces.AdminUsuarios;
import servicart.domain.mappers.PanelAdminMapperDomain;
import servicart.entities.Cliente;

import java.util.List;

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

        clienteDAO.save(new Cliente(dto.cedula(), dto.nombre(), dto.email(), dto.celular(), 1));
    }

    @Override
    public void editarUsuario(UsuarioDTOEntrada dto) {
        ClienteAdminDAO<Cliente> clienteDAO = FactoryDAO.getClienteAdminDAO();

        Cliente cliente = clienteDAO.findId(dto.cedula())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.cedula()));

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
}
