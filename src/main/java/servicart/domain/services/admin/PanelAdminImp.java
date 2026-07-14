package servicart.domain.services.admin;

import servicart.data.FactoryDAO;
import servicart.domain.dtos.retornos.ResumenAdminDTORetorno;
import servicart.domain.interfaces.PanelAdmin;
import servicart.domain.mappers.PanelAdminMapperDomain;
import servicart.domain.services.empresa.CorteService;
import servicart.entities.Cliente;
import servicart.entities.CorteServicio;
import servicart.entities.Factura;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PanelAdminImp implements PanelAdmin {

    @Override
    public ResumenAdminDTORetorno obtenerResumen() {
        List<Cliente> clientes = FactoryDAO.getDAO(Cliente.class).findAll();
        int usuariosActivos = (int) clientes.stream().filter(c -> c.getActivo() == 1).count();

        CorteService corteService = new CorteService(FactoryDAO.getDAO(CorteServicio.class));
        Set<String> cedulasCortadas = corteService.buscarCortados().stream()
                .map(corte -> corte.getContrato().getCliente().getCedula())
                .collect(Collectors.toSet());

        List<Factura> facturas = FactoryDAO.getDAO(Factura.class).findAll();
        Set<String> cedulasConMora = facturas.stream()
                .filter(Factura::estaVencida)
                .map(f -> f.getContrato().getCliente().getCedula())
                .collect(Collectors.toSet());

        return PanelAdminMapperDomain.resumenADTO(usuariosActivos, cedulasCortadas.size(), cedulasConMora.size(), FactoryDAO.obtenerModoActual());
    }
}