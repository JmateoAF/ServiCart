package servicart.entities;

import servicart.domain.interfaces.Identificable;
import servicart.entities.enums.CausaTerminacion;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Contrato implements Serializable, Identificable {
    private final LocalDateTime fechaInicio;
    private final ServicioCatalogo servicio;
    private final Cliente cliente;
    private int id;
    private LocalDateTime fechaFin; //Null mientras el contrato está activo
    private CausaTerminacion causaTerminacion;

    public Contrato(LocalDateTime fechaInicio, LocalDateTime fechaFin, CausaTerminacion causaTerminacion, ServicioCatalogo servicio, Cliente cliente) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.causaTerminacion = causaTerminacion;
        this.servicio = servicio;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }


    public CausaTerminacion getCausaTerminacion() {
        return causaTerminacion;
    }

    public void setCausaTerminacion(CausaTerminacion causaTerminacion) {
        this.causaTerminacion = causaTerminacion;
    }

    public ServicioCatalogo getServicio() {
        return servicio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public boolean estaActivo() {
        return causaTerminacion == CausaTerminacion.ACTIVO;
    }
}
