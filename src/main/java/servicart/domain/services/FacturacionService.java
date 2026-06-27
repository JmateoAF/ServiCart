package servicart.domain.services;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.entities.*;
import servicart.domain.models.enums.EstadoFactura;
import servicart.exceptions.ServiCartException;

import java.time.LocalDateTime;
import java.util.List;

/* Aplica el patrón Observer:
al emitir una factura notifica a todos los canales suscritos
(email, SMS, pantalla).
El Main suscribe los observadores:
facturacionService.agregarObservador(new NotificadorEmail());
facturacionService.agregarObservador(new NotificadorSMS()); */

public class FacturacionService extends SujetoNotificable {
    private final CrudDAO<Factura> facturaDAO;

    public FacturacionService(CrudDAO<Factura> facturaDAO) {
        this.facturaDAO = facturaDAO;
    }

    public Factura emitirFactura(Contrato contrato, double consumo) {
        if (contrato == null) throw new ServiCartException("El contrato no puede ser nulo", e);
        if (!contrato.estaActivo()) throw new ServiCartException("No se puede facturar un contrato terminado", e);

        double monto = contrato.getServicio().calcularMonto(consumo);
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime vencimiento = ahora.plusDays(30);
        LocalDateTime corte = ahora.plusDays(45);

        Factura factura = new Factura(ahora, vencimiento, corte, monto, contrato);
        facturaDAO.save(factura);

        // Observer, avisa a todos los canales suscritos
        notificarObservadores(factura);

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
}
