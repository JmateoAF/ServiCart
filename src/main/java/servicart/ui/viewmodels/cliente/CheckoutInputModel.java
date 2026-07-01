package servicart.ui.viewmodels.cliente;

public class CheckoutInputModel {
    private String modalidadPago;    // Valor del ComboBox: "Tarjeta de crédito", "PayPal", etc.
    private String referenciaPago;   // Número de tarjeta, cuenta PayPal...

    public CheckoutInputModel() {}

    public String getModalidadPago() { return modalidadPago; }
    public void setModalidadPago(String modalidadPago) { this.modalidadPago = modalidadPago; }

    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }
}
