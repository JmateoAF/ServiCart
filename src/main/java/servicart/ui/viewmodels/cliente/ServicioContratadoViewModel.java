package servicart.ui.viewmodels.cliente;

import java.util.ArrayList;
import java.util.List;

public class ServicioContratadoViewModel {
    private String nombreServicio;    // ej. "Agua – ETAPA"
    private String empresa;
    private String estadoContrato;    // "Activo", "Suspendido", etc.
    private String deudaTotal;        // formateado como "$ 45.20"
    private List<FacturaPendienteViewModel> listaFacturas;

    public ServicioContratadoViewModel() {
        listaFacturas = new ArrayList<>();
    }

    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getEstadoContrato() { return estadoContrato; }
    public void setEstadoContrato(String estadoContrato) { this.estadoContrato = estadoContrato; }

    public String getDeudaTotal() { return deudaTotal; }
    public void setDeudaTotal(String deudaTotal) { this.deudaTotal = deudaTotal; }

    public List<FacturaPendienteViewModel> getListaFacturas() { return listaFacturas; }
    public void setListaFacturas(List<FacturaPendienteViewModel> listaFacturas) {
        this.listaFacturas = listaFacturas;
    }
}