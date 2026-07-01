package servicart.ui.viewmodels.admin;

public class CorteResumenViewModel {
    private int totalCortados;
    private int totalEnMora;
    private String totalInteresesGenerados;

    public CorteResumenViewModel() {}

    public int getTotalCortados() { return totalCortados; }
    public void setTotalCortados(int totalCortados) { this.totalCortados = totalCortados; }

    public int getTotalEnMora() { return totalEnMora; }
    public void setTotalEnMora(int totalEnMora) { this.totalEnMora = totalEnMora; }

    public String getTotalInteresesGenerados() { return totalInteresesGenerados; }
    public void setTotalInteresesGenerados(String totalInteresesGenerados) { this.totalInteresesGenerados = totalInteresesGenerados; }
}