package servicart.domain.services.empresa;

import servicart.domain.interfaces.Observador;
import servicart.entities.Cliente;
import servicart.entities.Factura;

public class NotificadorService implements Observador {

    @Override
    public void actualizar(Factura factura) {
        Cliente cliente = factura.getContrato().getCliente();
        String servicio = factura.getContrato().getServicio().getEmpresa().getNombre() + " - " + factura.getContrato().getServicio().getTipo();
        String monto = String.format("$ %.2f", factura.getValorTotal());

        System.out.println("[SMS]   Hola " + cliente.getNombre() + ", tienes una nueva factura de " + servicio + " por " + monto);
        System.out.println("[EMAIL] Estimado/a " + cliente.getNombre() + ", se emitió tu factura de " + servicio + " por " + monto);
        System.out.println("[APP]   Nueva factura pendiente: " + servicio + " - " + monto);
    }

    public void notificarPago(Cliente cliente, double montoPagado) {
        String monto = String.format("$ %.2f", montoPagado);
        System.out.println("[SMS]   Hola " + cliente.getNombre() + ", registramos tu pago de " + monto + " ¡Gracias!");
        System.out.println("[EMAIL] Estimado/a " + cliente.getNombre() + ", tu pago de " + monto + " fue procesado con éxito");
        System.out.println("[APP]   Pago confirmado: " + monto);
    }
}