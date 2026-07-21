package servicart.data;

import servicart.data.binary.*;
import servicart.data.interfaces.CrudDAO;
import servicart.entities.*;
import servicart.entities.enums.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiPredicate;

public class DatosSeeder {

    public static void iniciar() {
        try {
            // Verificar si ya existen datos (por ejemplo, contratos)
            ContratoBinarioDAO contratoDAO = new ContratoBinarioDAO();
            List<Contrato> existentes;
            try {
                existentes = contratoDAO.findAllSinFiltro();
            } catch (Exception e) {
                // Si falla la lectura, asumimos que no hay datos y continuamos
                existentes = null;
            }

            if (existentes != null && !existentes.isEmpty()) {
                System.out.println("Datos ya existentes en archivos binarios. Se omite la siembra.");
                return;
            }
            Administrador admin1 = new Administrador("admin", "pass1");
            ponerEnArchivo(new AdminBinarioDAO(), List.of(admin1),
                    (existente, nuevo) -> existente.getUsuario().equals(nuevo.getUsuario()));

            Empresa emp1 = new Empresa("ETAPA");
            Empresa emp2 = new Empresa("CENTROSUR");
            Empresa emp3 = new Empresa("EMAC");
            Empresa emp4 = new Empresa("FIBRAMAX");
            EmpresaBinarioDAO empresaDAO =  new EmpresaBinarioDAO();
            ponerEnArchivo(empresaDAO, List.of(emp1, emp2, emp3, emp4),
                    (existente, nuevo) -> existente.getNombre().equals(nuevo.getNombre()));

            emp1 = buscarPorNombre(empresaDAO, "ETAPA");
            emp2 = buscarPorNombre(empresaDAO, "CENTROSUR");
            emp3 = buscarPorNombre(empresaDAO, "EMAC");
            emp4 = buscarPorNombre(empresaDAO, "FIBRAMAX");

            Cliente cl1 = new Cliente("0104636469", "Diego Quishpi", "diego.quishpi@mail.com", "0945825693", 1);
            Cliente cl2 = new Cliente("0102642568", "Elena Flores", "elena.flores@mail.com", "0984975208", 0);
            ponerEnArchivo(new ClienteBinarioDAO(), List.of(cl1, cl2),
                    (existente, nuevo) -> existente.getCedula().equals(nuevo.getCedula()));

            ServicioCatalogo agua1 = new ServicioCatalogo(emp1, TipoServicio.AGUA, TipoValorFactura.VARIABLE, 50.0, 0.05);
            agua1.setTarifaPorUnidad(0.85);
            ServicioCatalogo luz1 = new ServicioCatalogo(emp2, TipoServicio.LUZ, TipoValorFactura.VARIABLE, 80.0, 0.03);
            luz1.setTarifaPorUnidad(1.80);
            ServicioCatalogo luz2 = new ServicioCatalogo(emp2, TipoServicio.LUZ, TipoValorFactura.VARIABLE, 80.0, 0.03);
            luz2.setTarifaPorUnidad(1.85);
            ServicioCatalogo basura1 = new ServicioCatalogo(emp3, TipoServicio.BASURA, TipoValorFactura.FIJO, 30.0, 0.02);
            basura1.setTarifaFija(12.0);
            ServicioCatalogo basura2 = new ServicioCatalogo(emp3, TipoServicio.BASURA, TipoValorFactura.FIJO, 30.0, 0.02);
            basura2.setTarifaFija(12.50);
            ServicioCatalogo internet1 = new ServicioCatalogo(emp4, TipoServicio.INTERNET, TipoValorFactura.FIJO, 60.0, 0.04);
            internet1.setTarifaFija(35.0);
            ServicioCatalogo internet2 = new ServicioCatalogo(emp4, TipoServicio.INTERNET, TipoValorFactura.FIJO, 60.0, 0.04);
            internet2.setTarifaFija(45.0);
            ServicioCatalogo agua2 = new ServicioCatalogo(emp1, TipoServicio.AGUA, TipoValorFactura.VARIABLE, 50.0, 0.05);
            agua2.setTarifaPorUnidad(0.90);
            ServicioCatalogo internet3 = new ServicioCatalogo(emp4, TipoServicio.INTERNET, TipoValorFactura.FIJO, 60.0, 0.04);
            internet3.setTarifaFija(55.0);
            ServicioCatalogo basura3 = new ServicioCatalogo(emp3, TipoServicio.BASURA, TipoValorFactura.FIJO, 30.0, 0.02);
            basura3.setTarifaFija(13.0);

            ponerEnArchivo(new ServicioCatalogoBinarioDAO(),
                    List.of(agua1, luz1, luz2, basura1, basura2, internet1, internet2, agua2, internet3, basura3),
                    (existente, nuevo) ->
                            existente.getEmpresa().getNombre().equals(nuevo.getEmpresa().getNombre())
                                    && existente.getTipo() == nuevo.getTipo()
                                    && existente.getTarifaFija() == nuevo.getTarifaFija()
                                    && existente.getTarifaPorUnidad() == nuevo.getTarifaPorUnidad());

            Contrato contrato1 = new Contrato(
                    LocalDateTime.of(2024, 3, 15, 9, 0, 0),
                    null, CausaTerminacion.ACTIVO, luz1, cl1);
            Contrato contrato2 = new Contrato(
                    LocalDateTime.of(2025, 1, 10, 14, 30, 0),
                    null, CausaTerminacion.ACTIVO, internet1, cl1);
            Contrato contrato3 = new Contrato(
                    LocalDateTime.of(2024, 6, 20, 10, 0, 0),
                    null, CausaTerminacion.ACTIVO, agua2, cl2);

            ponerEnArchivo(new ContratoBinarioDAO(), List.of(contrato1, contrato2, contrato3),
                    (existente, nuevo) ->
                            existente.getCliente().getCedula().equals(nuevo.getCliente().getCedula())
                                    && existente.getServicio().getId() == nuevo.getServicio().getId()
                                    && existente.getFechaInicio().equals(nuevo.getFechaInicio()));

            // Facturas con fechas consistentes: emisión, vencimiento, fecha máxima de pago.
            Factura factura1 = new Factura(
                    LocalDateTime.of(2026, 6, 20, 0, 0, 0),       // emisión
                    LocalDateTime.of(2026, 7, 20, 23, 59, 59),    // vencimiento
                    LocalDateTime.of(2026, 8, 4, 0, 0, 0), 85.40, contrato1); // max pago
            factura1.setEstado(EstadoFactura.PENDIENTE);

            Factura factura2 = new Factura(
                    LocalDateTime.of(2026, 6, 20, 0, 0, 0),
                    LocalDateTime.of(2026, 7, 20, 23, 59, 59),
                    LocalDateTime.of(2026, 8, 6, 0, 0, 0), 35.0, contrato2);
            factura2.setEstado(EstadoFactura.PAGADA);

            // factura3: fechaMaxPago corregida a 2026-07-04 (posterior al vencimiento 2026-06-19)
            Factura factura3 = new Factura(
                    LocalDateTime.of(2026, 5, 20, 0, 0, 0),
                    LocalDateTime.of(2026, 6, 19, 23, 59, 59),
                    LocalDateTime.of(2026, 7, 4, 0, 0, 0), 22.50, contrato3);
            factura3.setEstado(EstadoFactura.VENCIDA);

            Factura factura4 = new Factura(
                    LocalDateTime.of(2026, 6, 20, 0, 0, 0),
                    LocalDateTime.of(2026, 7, 20, 23, 59, 59),
                    LocalDateTime.of(2026, 8, 6, 0, 0, 0), 24.75, contrato3);
            factura4.setEstado(EstadoFactura.PENDIENTE);

            Factura factura5 = new Factura(
                    LocalDateTime.of(2026, 4, 20, 0, 0, 0),
                    LocalDateTime.of(2026, 5, 20, 23, 59, 59),
                    LocalDateTime.of(2026, 6, 4, 0, 0, 0), 82.60, contrato1);
            factura5.setEstado(EstadoFactura.PAGADA);

            ponerEnArchivo(new FacturaBinarioDAO(), List.of(factura1, factura2, factura3, factura4, factura5),
                    (existente, nuevo) ->
                            existente.getContrato().getId() == nuevo.getContrato().getId()
                                    && existente.getFechaEmision().equals(nuevo.getFechaEmision()));

            // Abonos con fechas posteriores a la emisión de cada factura
            Abono abono1 = new Abono(50.0, LocalDateTime.of(2026, 6, 21, 10, 30, 0), true, factura1, ModalidadPago.TC);
            Abono abono2 = new Abono(35.0, LocalDateTime.of(2026, 6, 25, 14, 0, 0), true, factura2, ModalidadPago.TRANSFERENCIA);
            Abono abono3 = new Abono(22.50, LocalDateTime.of(2026, 6, 10, 9, 45, 0), false, factura3, ModalidadPago.TD);
            Abono abono4 = new Abono(24.75, LocalDateTime.of(2026, 6, 28, 11, 15, 0), false, factura4, ModalidadPago.PAYPAL);
            Abono abono5 = new Abono(35.40, LocalDateTime.of(2026, 6, 25, 16, 20, 0), true, factura1, ModalidadPago.DEBITO);
            Abono abono6 = new Abono(82.60, LocalDateTime.of(2026, 4, 25, 13, 0, 0), true, factura5, ModalidadPago.TC);

            ponerEnArchivo(new AbonoBinarioDAO(), List.of(abono1, abono2, abono3, abono4, abono5, abono6),
                    (existente, nuevo) ->
                            existente.getFactura().getId() == nuevo.getFactura().getId()
                                    && existente.getFechaPago().equals(nuevo.getFechaPago())
                                    && existente.getMonto() == nuevo.getMonto());

            // Intereses: fecha de cálculo después del vencimiento
            InteresMora interes1 = new InteresMora(14, 8.75, LocalDateTime.of(2026, 6, 29, 0, 0, 0), false, factura3);
            // interes2 ahora calculado el 2026-08-01 (después del vencimiento 2026-07-20)
            InteresMora interes2 = new InteresMora(8, 5.20, LocalDateTime.of(2026, 8, 1, 0, 0, 0), false, factura4);

            ponerEnArchivo(new InteresMoraBinarioDAO(), List.of(interes1, interes2),
                    (existente, nuevo) ->
                            existente.getFactura().getId() == nuevo.getFactura().getId()
                                    && existente.getFechaCalculo().equals(nuevo.getFechaCalculo()));

            // Corte de servicio después del vencimiento de la factura relacionada
            CorteServicio corte1 = new CorteServicio(LocalDateTime.of(2026, 6, 20, 8, 0, 0), contrato3, factura3);
            corte1.setEstadoCorte(EstadoCorte.CORTADO);

            ponerEnArchivo(new CorteServicioBinarioDAO(), List.of(corte1),
                    (existente, nuevo) ->
                            existente.getContrato().getId() == nuevo.getContrato().getId()
                                    && existente.getFechaCorte().equals(nuevo.getFechaCorte()));

            Carrito carrito1 = new Carrito(cl1);
            carrito1.agregarAbono(abono1);
            carrito1.agregarAbono(abono5);
            Carrito carrito2 = new Carrito(cl2);
            carrito2.agregarAbono(abono3);
            carrito2.agregarAbono(abono4);

            ponerEnArchivo(new CarritoBinarioDAO(), List.of(carrito1, carrito2),
                    (existente, nuevo) ->
                            existente.getCliente().getCedula().equals(nuevo.getCliente().getCedula()));

            System.out.println("Base de datos binaria inicializada con éxito");
        } catch (Exception e) {
            System.err.println("Error al sembrar los datos iniciales: " + e.getMessage());
        }
    }

    public static <T extends Serializable> void ponerEnArchivo(GenericBinarioDAO<T> dao, List<T> datos, BiPredicate<T, T> mismaClaveNatural) {
        List<T> existentes = dao.findAllSinFiltro();
        for (T entidad : datos) {
            boolean yaExiste = existentes.stream().anyMatch(e -> mismaClaveNatural.test(e, entidad));
            if (!yaExiste) dao.save(entidad);
        }
    }

    private static Empresa buscarPorNombre(CrudDAO<Empresa> dao, String nombre) {
        return dao.findAll().stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada: " + nombre));
    }
}