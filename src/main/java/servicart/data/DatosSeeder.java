package servicart.data;

import servicart.data.binary.*;
import servicart.data.interfaces.CrudDAO;
import servicart.entities.*;
import servicart.entities.enums.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiPredicate;

/* Datos mínimos indispensables: solo lo que la app no puede generar por sí sola
(credenciales, catálogo, clientes y sus contratos/facturas). Abonos, intereses de
mora y cortes de servicio NO se siembran aquí: el propio programa los calcula
(MoraService, CorteService, GestionAutomaticaEmpresaJob) apenas arranca, a partir
de las fechas de las facturas sembradas abajo. Como este método solo corre una vez
(se salta si ya hay contratos guardados), las fechas se calculan relativas a "ahora"
para que la demo siga siendo relevante sin importar cuándo se ejecute por primera vez */
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
                System.out.println("Datos ya existentes");
                return;
            }

            LocalDateTime ahora = LocalDateTime.now();

            Admin admin1 = new Admin("admin", "admin123");
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

            ServicioCatalogo agua1 = new ServicioCatalogo(emp1, TipoServicio.AGUA, TipoValorFactura.VARIABLE, 10.0, 0.05, 15);
            agua1.setTarifaPorUnidad(0.85);
            ServicioCatalogo luz1 = new ServicioCatalogo(emp2, TipoServicio.LUZ, TipoValorFactura.VARIABLE, 10.0, 0.03, 15);
            luz1.setTarifaPorUnidad(1.80);
            ServicioCatalogo basura1 = new ServicioCatalogo(emp3, TipoServicio.BASURA, TipoValorFactura.FIJO, 5.0, 0.02, 15);
            basura1.setTarifaFija(12.0);
            ServicioCatalogo internet1 = new ServicioCatalogo(emp4, TipoServicio.INTERNET, TipoValorFactura.FIJO, 15.0, 0.04, 15);
            internet1.setTarifaFija(35.0);

            ponerEnArchivo(new ServicioCatalogoBinarioDAO(),
                    List.of(agua1, luz1, basura1, internet1),
                    (existente, nuevo) ->
                            existente.getEmpresa().getNombre().equals(nuevo.getEmpresa().getNombre())
                                    && existente.getTipo() == nuevo.getTipo());

            // Contrato1 (LUZ) y Contrato2 (INTERNET) para el cliente activo de la demo;
            // Contrato3 (AGUA) ya terminado, del cliente inactivo (para poder demostrar
            // la reactivación de usuarios en el panel admin).
            Contrato contrato1 = new Contrato(ahora.minusMonths(16), null, CausaTerminacion.ACTIVO, luz1, cl1);
            Contrato contrato2 = new Contrato(ahora.minusMonths(10), null, CausaTerminacion.ACTIVO, internet1, cl1);
            Contrato contrato3 = new Contrato(ahora.minusMonths(20), ahora.minusMonths(2), CausaTerminacion.CLIENTE, agua1, cl2);

            ponerEnArchivo(new ContratoBinarioDAO(), List.of(contrato1, contrato2, contrato3),
                    (existente, nuevo) ->
                            existente.getCliente().getCedula().equals(nuevo.getCliente().getCedula())
                                    && existente.getServicio().getId() == nuevo.getServicio().getId()
                                    && existente.getFechaInicio().equals(nuevo.getFechaInicio()));

            // Tres facturas en tres estados distintos, para ver de inmediato (sin esperar
            // al ciclo automático diario) los tres caminos del negocio:
            //   1) pendiente y aún no vencida        -> se puede agregar al carrito y pagar
            //   2) ya vencida pero sin corte todavía -> MoraService le calculará el interés al iniciar la app
            //   3) ya pasó su fecha de corte         -> CorteService la cortará automáticamente al iniciar la app
            // Todas nacen en estado PENDIENTE, igual que las crea FacturacionService.emitirFactura();
            // es la propia gestión automática la que las hace avanzar a VENCIDA/cortada.
            Factura facturaPendiente = new Factura(
                    ahora.minusDays(10), ahora.plusDays(20), ahora.plusDays(35), 46.80, contrato1);

            Factura facturaVencidaSinCortar = new Factura(
                    ahora.minusDays(40), ahora.minusDays(10), ahora.plusDays(5), 35.0, contrato2);

            Factura facturaVencidaYaCortada = new Factura(
                    ahora.minusDays(75), ahora.minusDays(45), ahora.minusDays(30), 44.10, contrato1);

            ponerEnArchivo(new FacturaBinarioDAO(),
                    List.of(facturaPendiente, facturaVencidaSinCortar, facturaVencidaYaCortada),
                    (existente, nuevo) ->
                            existente.getContrato().getId() == nuevo.getContrato().getId()
                                    && existente.getFechaEmision().equals(nuevo.getFechaEmision()));

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
