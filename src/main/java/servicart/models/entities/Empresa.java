package servicart.models.entities;

import servicart.domain.interfaces.Identificable;
import java.io.Serializable;

/* Emitir facturas a los clientes
Enviar notificaciones (email, SMS)
Gestionar datos de contacto */

public class Empresa extends SujetoNotificable implements Serializable, Identificable {
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private String contacto;

    public Empresa(String nombre, String email, String telefono, String contacto) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.contacto = contacto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public void emitirFactura(Factura factura) { if (factura != null) notificarObservadores(factura); }

    public void enviarNotificacion(String mensaje) { System.out.println("Empresa " + nombre + " enviando notificación: " + mensaje); }
}