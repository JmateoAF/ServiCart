package servicart.domain.services.cliente;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.services.empresa.FacturacionService;
import servicart.entities.Factura;
import servicart.entities.InteresMora;
import servicart.entities.enums.EstadoFactura;
import java.time.LocalDateTime;
import java.util.Optional;

/* Aplica intereses progresivos por mora. A diferencia de la versión
   anterior, esto se llama todos los días mientras la factura no esté
   pagada — no solo una vez. Cada llamada RECALCULA el interés total
   desde fechaVencimiento hasta hoy (sobre valorBase, no sobre
   valorTotal, para no componer interés sobre interés) y actualiza
   el mismo registro InteresMora en vez de crear uno nuevo cada día */

public class MoraService {
    private final CrudDAO<InteresMora> interesMoraDAO;
    private final CrudDAO<Factura> facturaDAO;
    private final FacturacionService facturaService;

    public MoraService(CrudDAO<InteresMora> interesMoraDAO, CrudDAO<Factura> facturaDAO, FacturacionService facturaService) {
        this.interesMoraDAO = interesMoraDAO;
        this.facturaDAO = facturaDAO;
        this.facturaService = facturaService;
    }

    public void aplicarMora(Factura factura) {
        long diasRetraso = factura.diasDeRetraso();
        if (diasRetraso <= 0) return;

        double interesTotal = factura.interesAcumulado();

        buscarPorFactura(factura.getId())
                .map(existente -> actualizarRegistro(existente, (int) diasRetraso, interesTotal, LocalDateTime.now()))
                .orElseGet(() -> crearRegistro((int) diasRetraso, interesTotal, LocalDateTime.now(), factura));
        factura.setValorTotal(factura.getValorBase() + interesTotal);

        if (factura.getEstado() != EstadoFactura.VENCIDA) {
            facturaService.marcarComoVencida(factura);
            facturaDAO.update(factura);// primera vez: cambia estado + persiste
        } else {
            facturaDAO.update(factura); // ya estaba vencida: solo persiste el nuevo valorTotal
        }

    }

    private InteresMora actualizarRegistro(InteresMora mora, int diasRetraso, double interesTotal, LocalDateTime ahora) {
        mora.setDiasRetraso(diasRetraso);
        mora.setInteresAcumulado(interesTotal);
        mora.setFechaCalculo(ahora);
        mora.setAplicadoAFactura(true);
        interesMoraDAO.update(mora);
        return mora;
    }

    private InteresMora crearRegistro(int diasRetraso, double interesTotal, LocalDateTime ahora, Factura factura) {
        InteresMora mora = new InteresMora(diasRetraso, interesTotal, ahora, true, factura);
        interesMoraDAO.save(mora);
        return mora;
    }

    private Optional<InteresMora> buscarPorFactura(int idFactura) {
        return interesMoraDAO.findAll().stream()
                .filter(m -> m.getFactura().getId() == idFactura)
                .findFirst();
    }
}