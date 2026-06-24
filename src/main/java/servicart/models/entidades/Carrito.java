package servicart.models.entidades;

import java.io.Serializable;

public class Carrito implements Serializable {
    private int id;
    private double montoAbono;
    private final Cliente cliente;
    private final Abono abono;

    public Carrito(double montoAbono, Cliente cliente, Abono abono) {
        this.montoAbono = montoAbono;
        this.cliente = cliente;
        this.abono = abono;
    }

    public int getId() { return id; }
    public void setId(int id) {this.id = id;}

    public double getMontoAbono() { return montoAbono; }
    public void setMontoAbono(double montoAbono) { this.montoAbono = montoAbono; }

    public Cliente getCliente() { return cliente; }

    public Abono getAbono() { return abono; }
}
