package servicart.domain.models.servicios;

import servicart.domain.models.enums.Empresa;
import servicart.domain.models.enums.TipoServicio;
import servicart.domain.models.enums.TipoValorFactura;

import java.io.Serializable;

/*
 * Representa un servicio del catálogo (agua, luz, basura, internet).
 * Contiene las tarifas, empresa asociada y delega el cálculo del monto
 * en una estrategia (fijo/variable) según el tipo de valor.
 */

public class ServicioCatalogo implements Serializable {
    private int id;
    private double tarifaFija;
    private double tarifaPorUnidad;
    private TipoServicio tipo;                // AGUA, LUZ, BASURA, INTERNET
    private TipoValorFactura tipoValor;       // FIJO o VARIABLE
    private double costoReactivacion;
    private double tasaInteresDiario;
    private final Empresa empresa;

    private CalculoStrategy estrategia;     // Estrategia de cálculo (transitoria o calculada)

    public ServicioCatalogo(int id, TipoServicio tipo, TipoValorFactura tipoValor, double costoReactivacion, double tasaInteresDiario, Empresa empresa) {
        this.id = id;
        this.tipo = tipo;
        this.tipoValor = tipoValor;
        this.costoReactivacion = costoReactivacion;
        this.tasaInteresDiario = tasaInteresDiario;
        this.empresa = empresa;
        asignarEstrategia();
    }

    // Asigna la estrategia correcta según el tipo de valor
    private void asignarEstrategia() {
        if (tipoValor != null) {
            if (tipoValor == TipoValorFactura.FIJO) {
                this.estrategia = new CalculoFijo();
            } else if (tipoValor == TipoValorFactura.VARIABLE) {
                this.estrategia = new CalculoVariable();
            }
        }
        // Si tipoValor es null (caso raro), estrategia se mantiene null
    }

    /**
     * Calcula el monto de la factura para un consumo dado.
     * Se basa en la estrategia asignada.
     */


    public double calcularMonto(double consumo) {
        if (estrategia == null) {
            throw new IllegalStateException("Estrategia de cálculo no asignada para el servicio " + id);
        }
        return estrategia.calcular(consumo, this);
    }

    public int getId() { return id; }

    public void setId(int id) {this.id = id;}

    public double getTarifaFija() { return tarifaFija; }
    public void setTarifaFija(double tarifaFija) { this.tarifaFija = tarifaFija; }

    public double getTarifaPorUnidad() { return tarifaPorUnidad; }
    public void setTarifaPorUnidad(double tarifaPorUnidad) { this.tarifaPorUnidad = tarifaPorUnidad; }

    public TipoServicio getTipo() { return tipo; }
    public void setTipo(TipoServicio tipo) { this.tipo = tipo; }

    public TipoValorFactura getTipoValor() { return tipoValor; }
    public void setTipoValorFactura(TipoValorFactura tipoValor) {
        this.tipoValor = tipoValor;
        asignarEstrategia();  // reasignar al cambiar el tipo
    }

    public double getCostoReactivacion() { return costoReactivacion; }
    public void setCostoReactivacion(double costoReactivacion) { this.costoReactivacion = costoReactivacion; }

    public double getTasaInteresDiario() { return tasaInteresDiario; }
    public void setTasaInteresDiario(double tasaInteresDiario) { this.tasaInteresDiario = tasaInteresDiario; }

    public Empresa getEmpresa() { return empresa; }

    // La estrategia no necesita getter público, es interna
}
