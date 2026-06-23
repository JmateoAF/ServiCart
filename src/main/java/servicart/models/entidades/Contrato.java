package servicart.models.entidades;

import servicart.models.enums.CausaTerminacion;
import servicart.models.servicios.ServicioCatalogo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Contrato implements Serializable {
    private final int id;
    private final LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private CausaTerminacion causaTerminacion;
    private final LocalDateTime fechaTerminacion;
    private final ServicioCatalogo servicio;
    private final Cliente cliente;

    public Contrato(int id, LocalDateTime fechaInicio, LocalDateTime fechaFin, LocalDateTime fechaTerminacion, ServicioCatalogo servicio, Cliente cliente) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaTerminacion = fechaTerminacion;
        this.servicio = servicio;
        this.cliente = cliente;
        setCausaTerminacion(CausaTerminacion.ACTIVO);
    }

    public int getId() { return id; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }


    public CausaTerminacion getCausaTerminacion() { return causaTerminacion; }
    public void setCausaTerminacion(CausaTerminacion causaTerminacion) { this.causaTerminacion = causaTerminacion; }

    public LocalDateTime getFechaTerminacion() { return fechaTerminacion; }

    public ServicioCatalogo getServicio() { return servicio; }

    public Cliente getCliente() { return cliente; }
}
