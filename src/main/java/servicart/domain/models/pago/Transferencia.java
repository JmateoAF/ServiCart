package servicart.domain.models.pago;

import servicart.domain.models.enums.ModalidadPago;

public class Transferencia extends MetodoPago{
    @Override
    public ModalidadPago getTipo() {
        return ModalidadPago.TRANSFERENCIA;
    }

}
