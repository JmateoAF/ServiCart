package servicart.domain.models.entidades;

import servicart.domain.models.enums.CausaTerminacion;
import servicart.domain.models.servicios.ServicioCatalogo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Contrato implements Serializable {
    private final int id;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaFin;
    private CausaTerminacion causaTerminacion;
    private final LocalDateTime fechaTerminacion;
    private final ServicioCatalogo servicio;
    private final Cliente cliente;

    public Contrato(int id, LocalDateTime fechaInicio, LocalDateTime fechaFin, CausaTerminacion causaTerminacion, LocalDateTime fechaTerminacion, ServicioCatalogo servicio, Cliente cliente) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.causaTerminacion = causaTerminacion;
        this.fechaTerminacion = fechaTerminacion;
        this.servicio = servicio;
        this.cliente = cliente;
    }

    public int getId() { return id; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }

    public CausaTerminacion getCausaTerminacion() { return causaTerminacion; }
    public void setCausaTerminacion(CausaTerminacion causaTerminacion) { this.causaTerminacion = causaTerminacion; }

    public LocalDateTime getFechaTerminacion() { return fechaTerminacion; }

    public ServicioCatalogo getServicio() { return servicio; }

    public Cliente getCliente() { return cliente; }
}
