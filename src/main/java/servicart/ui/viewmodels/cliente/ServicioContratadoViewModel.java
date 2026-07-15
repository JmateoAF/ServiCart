package servicart.ui.viewmodels.cliente;

import java.util.ArrayList;
import java.util.List;

public class ServicioContratadoViewModel {
    private int idContrato;
    private String nombreServicio;
    private String empresa;
    private boolean conMora;
    private boolean estaCortado;
    private double costoReactivacion; //valor crudo, para cálculos
    private String costoReactivacionTexto; //solo para mostrar en pantalla
    private String subtotalServicioTexto;
    private List<FacturaPendienteViewModel> listaFacturas;

    public ServicioContratadoViewModel() {
        listaFacturas = new ArrayList<>();
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public boolean isConMora() {
        return conMora;
    }

    public void setConMora(boolean conMora) {
        this.conMora = conMora;
    }

    public boolean isEstaCortado() {
        return estaCortado;
    }

    public void setEstaCortado(boolean estaCortado) {
        this.estaCortado = estaCortado;
    }

    public double getCostoReactivacion() {
        return costoReactivacion;
    }

    public void setCostoReactivacion(double costoReactivacion) {
        this.costoReactivacion = costoReactivacion;
    }

    public String getCostoReactivacionTexto() {
        return costoReactivacionTexto;
    }

    public void setCostoReactivacionTexto(String costoReactivacionTexto) {
        this.costoReactivacionTexto = costoReactivacionTexto;
    }

    public String getSubtotalServicioTexto() {
        return subtotalServicioTexto;
    }

    public void setSubtotalServicioTexto(String subtotalServicioTexto) {
        this.subtotalServicioTexto = subtotalServicioTexto;
    }

    public List<FacturaPendienteViewModel> getListaFacturas() {
        return listaFacturas;
    }

    public void setListaFacturas(List<FacturaPendienteViewModel> listaFacturas) {
        this.listaFacturas = listaFacturas;
    }
}