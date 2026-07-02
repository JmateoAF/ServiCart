package servicart.ui.controllers;

import servicart.domain.services.notifiers.NotificadorEmail;
import servicart.domain.services.notifiers.NotificadorPantalla;
import servicart.domain.services.notifiers.NotificadorSMS;
import servicart.domain.services.FacturacionService;

public final class GestorNotificacion {
    private static FacturacionService facturacionService;

    public static void inicializar() {
        //facturacionService = new FacturacionService(FactoryDAO.facturaDAO());
        facturacionService.agregarObservador(new NotificadorEmail());
        facturacionService.agregarObservador(new NotificadorSMS());
        facturacionService.agregarObservador(new NotificadorPantalla(msg -> System.out.println("[PANTALLA] " + msg)));
    }

    public static FacturacionService getFacturacionService() {
        return facturacionService;
    }
}