package servicart.models.catalog;

import servicart.domain.interfaces.CalculoStrategy;
import servicart.domain.interfaces.Identificable;
import servicart.models.entities.Empresa;
import servicart.models.enums.TipoServicio;
import servicart.models.enums.TipoValorFactura;
import java.io.Serializable;

/*
 * Representa un servicio del catálogo (agua, luz, basura, internet).
 * Contiene las tarifas, empresa asociada y delega el cálculo del monto
 * en una estrategia (fijo/variable) según el tipo de valor.
 */

public class ServicioCatalogo implements Serializable, Identificable {
    private int id;
    private Empresa empresa;
    private TipoServicio tipo;
    private TipoValorFactura tipoValor;
    private double tarifaFija;
    private double tarifaPorUnidad;
    private double costoReactivacion;
    private double tasaInteresDiario;
    private transient CalculoStrategy estrategia;    // Estrategia de cálculo (transitoria o calculada)

    public ServicioCatalogo(Empresa empresa, TipoServicio tipo, TipoValorFactura tipoValor, double costoReactivacion, double tasaInteresDiario) {
        this.empresa = empresa;
        this.tipo = tipo;
        this.tipoValor = tipoValor;
        this.costoReactivacion = costoReactivacion;
        this.tasaInteresDiario = tasaInteresDiario;
        this.tarifaFija = 0.0;
        this.tarifaPorUnidad = 0.0;
        asignarEstrategia();
    }

    //Asigna la estrategia correcta según el tipo de valor
    private void asignarEstrategia() {
        this.estrategia = switch (tipoValor) {
            case FIJO -> new CalculoFijo();
            case VARIABLE -> new CalculoVariable();
        };
    }

    /* Calcula el monto de la factura para un consumo dado.
    Se basa en la estrategia asignada*/

    public double calcularMonto(double consumo) {
        return estrategia.calcular(consumo, this);
    }

    public int getId() { return id; }
    public void setId(int id) {this.id = id;}

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public TipoServicio getTipo() { return tipo; }
    public void setTipo(TipoServicio tipo) { this.tipo = tipo; }

    public TipoValorFactura getTipoValor() { return tipoValor; }
    public void setTipoValorFactura(TipoValorFactura tipoValor) {
        this.tipoValor = tipoValor;
        asignarEstrategia();  // reasignar al cambiar el tipo
    }

    public double getTarifaFija() { return tarifaFija; }
    public void setTarifaFija(double tarifaFija) { this.tarifaFija = tarifaFija; }

    public double getTarifaPorUnidad() { return tarifaPorUnidad; }

    public void setTarifaPorUnidad(double tarifaPorUnidad) { this.tarifaPorUnidad = tarifaPorUnidad; }

    public double getCostoReactivacion() { return costoReactivacion; }
    public void setCostoReactivacion(double costoReactivacion) { this.costoReactivacion = costoReactivacion; }

    public double getTasaInteresDiario() { return tasaInteresDiario; }
    public void setTasaInteresDiario(double tasaInteresDiario) { this.tasaInteresDiario = tasaInteresDiario; }
}
