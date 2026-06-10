package servicart.domain.models.pago;

import servicart.domain.models.enums.ModalidadPago;

public class TarjetaCredito extends MetodoPago{
    @Override
    public ModalidadPago getTipo() {
        return ModalidadPago.TC;
    }

}
