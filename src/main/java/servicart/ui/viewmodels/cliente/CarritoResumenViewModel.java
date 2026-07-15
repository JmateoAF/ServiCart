package servicart.ui.viewmodels.cliente;

import java.util.ArrayList;
import java.util.List;

public class CarritoResumenViewModel {
    private String subtotal;
    private String total;
    private List<AbonoCarritoViewModel> items;

    public CarritoResumenViewModel() {
        items = new ArrayList<>();
    }

    public void setCantidadItems() {
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<AbonoCarritoViewModel> getItems() {
        return items;
    }

    public void setItems(List<AbonoCarritoViewModel> items) {
        this.items = items;
    }
}