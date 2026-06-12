package servicart.domain.models.servicios;

import servicart.domain.models.enums.TipoServicio;
import servicart.domain.models.enums.TipoValorFactura;

import java.io.Serializable;

/**
 * Representa un servicio del catálogo (agua, luz, basura, internet).
 * Contiene las tarifas, empresa asociada y delega el cálculo del monto
 * en una estrategia (fijo/variable) según el tipo de valor.
 */
public class ServicioCatalogo implements Serializable {

    private int id;
    private TipoServicio tipo;                // AGUA, LUZ, BASURA, INTERNET
    private TipoValorFactura tipoValor;       // FIJO o VARIABLE
    private int tarifaFija;                   // centavos
    private int tarifaPorUnidad;              // centavos por unidad
    private int costoReactivacion;            // centavos
    private double tasaInteresDiario;         // ej. 0.15
    private int idEmpresa;                    // FK a Empresa

    private EstrategiaCalculo estrategia;     // Estrategia de cálculo (transitoria o calculada)

    // Constructor vacío necesario para serialización
    public ServicioCatalogo() {
        asignarEstrategia();
    }

    // Constructor completo
    public ServicioCatalogo(int id, TipoServicio tipo, TipoValorFactura tipoValor,
                            int tarifaFija, int tarifaPorUnidad, int costoReactivacion,
                            double tasaInteresDiario, int idEmpresa) {
        this.id = id;
        this.tipo = tipo;
        this.tipoValor = tipoValor;
        this.tarifaFija = tarifaFija;
        this.tarifaPorUnidad = tarifaPorUnidad;
        this.costoReactivacion = costoReactivacion;
        this.tasaInteresDiario = tasaInteresDiario;
        this.idEmpresa = idEmpresa;
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

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public TipoServicio getTipo() { return tipo; }
    public void setTipo(TipoServicio tipo) { this.tipo = tipo; }

    public TipoValorFactura getTipoValor() { return tipoValor; }
    public void setTipoValor(TipoValorFactura tipoValor) {
        this.tipoValor = tipoValor;
        asignarEstrategia();  // reasignar al cambiar el tipo
    }

    public int getTarifaFija() { return tarifaFija; }
    public void setTarifaFija(int tarifaFija) { this.tarifaFija = tarifaFija; }

    public int getTarifaPorUnidad() { return tarifaPorUnidad; }
    public void setTarifaPorUnidad(int tarifaPorUnidad) { this.tarifaPorUnidad = tarifaPorUnidad; }

    public int getCostoReactivacion() { return costoReactivacion; }
    public void setCostoReactivacion(int costoReactivacion) { this.costoReactivacion = costoReactivacion; }

    public double getTasaInteresDiario() { return tasaInteresDiario; }
    public void setTasaInteresDiario(double tasaInteresDiario) { this.tasaInteresDiario = tasaInteresDiario; }

    public int getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(int idEmpresa) { this.idEmpresa = idEmpresa; }

    // La estrategia no necesita getter público, es interna.
}
