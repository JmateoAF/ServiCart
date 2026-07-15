package servicart.entities;

import servicart.domain.interfaces.Identificable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carrito implements Serializable, Identificable {
    private final List<Abono> abonos;
    private int id;
    private Cliente cliente;

    public Carrito(Cliente cliente) {
        this.cliente = cliente;
        this.abonos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Abono> getAbonos() {
        return Collections.unmodifiableList(abonos);
    }

    public void agregarAbono(Abono abono) {
        if (abono != null && !abonos.contains(abono)) abonos.add(abono);
    }

    public void vaciar() {
        abonos.clear();
    }

    public boolean estaVacio() {
        return abonos.isEmpty();
    }

    public double getTotal() {
        return abonos.stream().mapToDouble(Abono::getMonto).sum();
    }
}
