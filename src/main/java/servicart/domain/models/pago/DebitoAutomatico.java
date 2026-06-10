package servicart.domain.models.pago;

import servicart.domain.models.enums.ModalidadPago;

public class DebitoAutomatico extends MetodoPago{
    @Override
    public ModalidadPago getTipo() {
        return ModalidadPago.DEBITO;
    }

}
