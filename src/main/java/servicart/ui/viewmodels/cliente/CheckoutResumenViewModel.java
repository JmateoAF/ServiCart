package servicart.ui.viewmodels.cliente;

import java.util.ArrayList;
import java.util.List;

public class CheckoutResumenViewModel {
    private List<AbonoCarritoViewModel> items;
    private String totalFinal;

    public CheckoutResumenViewModel() {
        items = new ArrayList<>();
    }

    public List<AbonoCarritoViewModel> getItems() { return items; }
    public void setItems(List<AbonoCarritoViewModel> items) { this.items = items; }

    public String getTotalFinal() { return totalFinal; }
    public void setTotalFinal(String totalFinal) { this.totalFinal = totalFinal; }
}