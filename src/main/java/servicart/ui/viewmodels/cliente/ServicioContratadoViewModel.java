package servicart.ui.viewmodels.cliente;

import java.util.ArrayList;
import java.util.List;

public class ServicioContratadoViewModel {
    private int idContrato;
    private String nombreServicio;
    private String icono;
    private String empresa;
    private double deudaTotal;
    private boolean conMora;
    private List<FacturaPendienteViewModel> listaFacturas;

    public ServicioContratadoViewModel() { listaFacturas = new ArrayList<>(); }

    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }

    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public double getDeudaTotal() { return deudaTotal; }
    public void setDeudaTotal(double deudaTotal) { this.deudaTotal = deudaTotal; }

    public boolean isConMora() { return conMora; }
    public void setConMora(boolean conMora) { this.conMora = conMora; }

    public List<FacturaPendienteViewModel> getListaFacturas() { return listaFacturas; }
    public void setListaFacturas(List<FacturaPendienteViewModel> listaFacturas) {
        this.listaFacturas = listaFacturas;
    }
}