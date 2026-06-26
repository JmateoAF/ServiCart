package servicart.data.binary;

import servicart.domain.models.servicios.ServicioCatalogo;
import servicart.domain.models.enums.Empresa;
import servicart.domain.models.enums.TipoServicio;
import servicart.domain.models.enums.TipoValorFactura;

import java.util.ArrayList;
import java.util.List;

public class ServicioCatalogoBinarioDAO extends GenericBinarioDAO<ServicioCatalogo> {

    public ServicioCatalogoBinarioDAO() {
        super("bin/servicioCatalogo.bin");
        inicializarDatos();
    }

    @Override
    protected String getId(ServicioCatalogo entidad) {
        return String.valueOf(entidad.getId());
    }

    private void inicializarDatos() {
        List<ServicioCatalogo> existentes = leerTodos();
        if (!existentes.isEmpty()) {
            cache = existentes;
            return;
        }

        List<ServicioCatalogo> iniciales = new ArrayList<>();

        // Agua: tarifa fija $25.0, reactivación $50.0, tasa diaria 0.05 (5%), empresa ETAPA
        ServicioCatalogo agua = new ServicioCatalogo(
                1, TipoServicio.AGUA, TipoValorFactura.FIJO,
                50.0, 0.05, Empresa.ETAPA);
        agua.setTarifaFija(25.0);
        iniciales.add(agua);

        // Luz: tarifa por unidad $1.8, reactivación $80.0, tasa 0.03, empresa CENTROSUR
        ServicioCatalogo luz = new ServicioCatalogo(
                2, TipoServicio.LUZ, TipoValorFactura.VARIABLE,
                80.0, 0.03, Empresa.CENTROSUR);
        luz.setTarifaPorUnidad(1.8);
        iniciales.add(luz);

        // Basura: tarifa fija $12.0, reactivación $30.0, tasa 0.02, empresa EMAC
        ServicioCatalogo basura = new ServicioCatalogo(
                3, TipoServicio.BASURA, TipoValorFactura.FIJO,
                30.0, 0.02, Empresa.EMAC);
        basura.setTarifaFija(12.0);
        iniciales.add(basura);

        // Internet: tarifa fija $35.0, reactivación $60.0, tasa 0.04, empresa FIBRAMAX
        ServicioCatalogo internet = new ServicioCatalogo(
                4, TipoServicio.INTERNET,
                TipoValorFactura.FIJO,
                60.0,
                0.04, Empresa.FIBRAMAX);

        internet.setTarifaFija(35.0);
        iniciales.add(internet);

        guardarTodos(iniciales);
        cache = iniciales;
    }
}