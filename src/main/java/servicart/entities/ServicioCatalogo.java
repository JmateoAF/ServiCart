package servicart.entities;

import servicart.domain.interfaces.CalculoStrategy;
import servicart.domain.interfaces.Identificable;
import servicart.entities.enums.TipoServicio;
import servicart.entities.enums.TipoValorFactura;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;

public class ServicioCatalogo implements Serializable, Identificable {
    private int id;
    private Empresa empresa;
    private TipoServicio tipo;
    private TipoValorFactura tipoValor;
    private double tarifaFija;
    private double tarifaPorUnidad;
    private double costoReactivacion;
    private double tasaInteresDiario;
    private int diasParaCorte; // días entre el vencimiento y el corte del servicio; configurable por servicio
    private transient CalculoStrategy estrategia;

    public ServicioCatalogo(Empresa empresa, TipoServicio tipo, TipoValorFactura tipoValor, double costoReactivacion, double tasaInteresDiario, int diasParaCorte) {
        this.empresa = empresa;
        this.tipo = tipo;
        this.tipoValor = tipoValor;
        this.costoReactivacion = costoReactivacion;
        this.tasaInteresDiario = tasaInteresDiario;
        this.diasParaCorte = diasParaCorte;
        this.tarifaFija = 0.0;
        this.tarifaPorUnidad = 0.0;
        asignarEstrategia();
    }

    private void asignarEstrategia() {
        this.estrategia = switch (tipoValor) {
            case FIJO -> new ServicioFijo();
            case VARIABLE -> new ServicioVariable();
        };
    }

    // Se ejecuta automáticamente cada vez que Java deserializa este objeto desde él .bin
    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        asignarEstrategia();
    }

    public double calcularMonto(double consumo) { return estrategia.calcular(consumo, this); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public TipoServicio getTipo() { return tipo; }
    public void setTipo(TipoServicio tipo) { this.tipo = tipo; }

    public TipoValorFactura getTipoValor() { return tipoValor; }
    public void setTipoValorFactura(TipoValorFactura tipoValor) {
        this.tipoValor = tipoValor;
        asignarEstrategia();
    }

    public double getTarifaFija() { return tarifaFija; }
    public void setTarifaFija(double tarifaFija) { this.tarifaFija = tarifaFija; }

    public double getTarifaPorUnidad() { return tarifaPorUnidad; }
    public void setTarifaPorUnidad(double tarifaPorUnidad) { this.tarifaPorUnidad = tarifaPorUnidad; }

    public double getCostoReactivacion() { return costoReactivacion; }
    public void setCostoReactivacion(double costoReactivacion) { this.costoReactivacion = costoReactivacion; }

    public double getTasaInteresDiario() { return tasaInteresDiario; }
    public void setTasaInteresDiario(double tasaInteresDiario) { this.tasaInteresDiario = tasaInteresDiario; }

    public int getDiasParaCorte() { return diasParaCorte; }
    public void setDiasParaCorte(int diasParaCorte) { this.diasParaCorte = diasParaCorte; }
}