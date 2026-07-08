package servicart.domain.services.empresa;

import servicart.data.interfaces.CrudDAO;
import servicart.entities.*;
import servicart.entities.enums.EstadoFactura;
import java.time.LocalDateTime;
import java.util.List;

public class FacturacionService {
    private final CrudDAO<Factura> facturaDAO;

    public FacturacionService(CrudDAO<Factura> facturaDAO) {
        this.facturaDAO = facturaDAO;
    }

    public Factura emitirFactura(Contrato contrato, double consumo) {
        double monto = contrato.getServicio().calcularMonto(consumo);
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime vencimiento = ahora.plusDays(30);
        LocalDateTime corte = ahora.plusDays(45);

        Factura factura = new Factura(ahora, vencimiento, corte, monto, contrato);
        facturaDAO.save(factura);
        return factura;
    }

    public List<Factura> buscarPorContrato(int contratoId) {
        return facturaDAO.findAll().stream().filter(f -> f.getContrato().getId() == contratoId).toList();
    }

    public List<Factura> buscarPendientesPorCliente(String cedula) {
        return facturaDAO.findAll().stream().filter(f -> f.getContrato().getCliente().getCedula().equals(cedula)).filter(f -> f.getEstado() != EstadoFactura.PAGADA).toList();
    }

    public void marcarComoPagada(Factura factura) {
        factura.setEstado(EstadoFactura.PAGADA);
        facturaDAO.update(factura);
    }

    public void marcarComoVencida(Factura factura) {
        factura.setEstado(EstadoFactura.VENCIDA);
        facturaDAO.update(factura);
    }

    public void actualizarValorTotal(Factura factura, double nuevoValorTotal) {
        factura.setValorTotal(nuevoValorTotal);
        facturaDAO.update(factura);
    }
}