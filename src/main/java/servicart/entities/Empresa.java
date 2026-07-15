package servicart.entities;

import servicart.domain.interfaces.Identificable;

import java.io.Serializable;

/* Emitir facturas a los clientes
Enviar notificaciones (email, SMS)
Gestionar datos de contacto */

public class Empresa extends SujetoNotificable implements Serializable, Identificable {
    private int id;
    private String nombre;

    public Empresa(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void emitirFactura(Factura factura) {
        if (factura != null) notificarObservadores(factura);
    }
}