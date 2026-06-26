package servicart.data;

import servicart.data.interfaces.CrudDAO;
import servicart.domain.models.enums.Empresa;
import servicart.domain.models.enums.TipoServicio;
import servicart.domain.models.enums.TipoValorFactura;
import servicart.domain.models.catalog.ServicioCatalogo;
import java.util.List;

/* Siembra los 4 servicios del catálogo si el archivo/tabla está vacío
Se llama UNA SOLA VEZ desde el Main al arrancar la aplicación
Por qué existe esta clase y no está en ServicioCatalogoBinarioDAO:
Un DAO solo sabe guardar y recuperar datos. Decidir QUÉ datos existen
al inicio es una responsabilidad de arranque (startup concern),
no de persistencia
Corrección de spec respecto a la versión anterior:
AGUA y LU -> VARIABLE (dependen del consumo medido)
BASURA e INTERNET -> FIJO(tarifa plana mensual) */

public class CatalogoSeeder {
    private final CrudDAO<ServicioCatalogo> dao;

    public CatalogoSeeder(CrudDAO<ServicioCatalogo> dao) { this.dao = dao; }

    public void sembrar() {
        if (!dao.findAll().isEmpty()) return; // Ya hay datos, no sobreescribir

        // El ID lo asigna GenericBinarioDAO automáticamente (autoincremento)
        // Se pasa 0 como placeholder

        ServicioCatalogo agua = new ServicioCatalogo(0, TipoServicio.AGUA, TipoValorFactura.VARIABLE, 50.0, 0.05, Empresa.ETAPA);
        agua.setTarifaPorUnidad(0.85); // $0.85 por m³

        ServicioCatalogo luz = new ServicioCatalogo(0, TipoServicio.LUZ, TipoValorFactura.VARIABLE, 80.0, 0.03, Empresa.CENTROSUR);
        luz.setTarifaPorUnidad(1.80); // $1.80 por kWh

        ServicioCatalogo basura = new ServicioCatalogo(0, TipoServicio.BASURA, TipoValorFactura.FIJO, 30.0, 0.02, Empresa.EMAC);
        basura.setTarifaFija(12.0); // $12.00 mensual fijo

        ServicioCatalogo internet = new ServicioCatalogo(0, TipoServicio.INTERNET, TipoValorFactura.FIJO, 60.0, 0.04, Empresa.FIBRAMAX);
        internet.setTarifaFija(35.0); // $35.00 mensual fijo

        for (ServicioCatalogo s : List.of(agua, luz, basura, internet)) dao.save(s);
    }
}