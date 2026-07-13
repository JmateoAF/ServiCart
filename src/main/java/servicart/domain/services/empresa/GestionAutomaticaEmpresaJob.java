package servicart.domain.services.empresa;

import servicart.data.FactoryDAO;
import servicart.data.interfaces.CrudDAO;
import servicart.domain.services.cliente.MoraService;
import servicart.entities.*;
import servicart.entities.enums.CausaTerminacion;
import servicart.entities.enums.EstadoFactura;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GestionAutomaticaEmpresaJob {

    private static final int DIA_FACTURACION_MES = 20; // día fijo del mes en que se emite
    private static final long DIAS_CORTADO_PARA_TERMINAR = 30;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "hilo-gestion-automatica-empresa");
        t.setDaemon(true);
        return t;
    });

    private static final Object CANDADO_BD = new Object();

    public void iniciar() {
        scheduler.scheduleAtFixedRate(this::ejecutarCiclo, 0, 1, TimeUnit.DAYS);
    }

    public void detener() { scheduler.shutdownNow(); }

    private void ejecutarCiclo() {
        synchronized (CANDADO_BD) {
            ejecutarTarea("emisión de facturas", this::emitirFacturasPendientes);
            ejecutarTarea("aplicación de mora", this::aplicarMoraAVencidas);
            ejecutarTarea("corte de servicios", this::cortarServiciosMorosos);
            ejecutarTarea("terminación de contratos", this::terminarContratosPorCorteProlongado);
        }
    }

    private void ejecutarTarea(String nombre, Runnable tarea) {
        try {
            tarea.run();
        } catch (Exception e) {
            System.err.println("Gestión automática de la empresa — falló '" + nombre + "': " + e.getMessage());
        }
    }

    //1) EMITIR FACTURAS — el día 20 de cada mes
    private void emitirFacturasPendientes() {
        // Regla de calendario: solo se emite el día 20, el resto de los
        // días del mes esta tarea no hace nada (pero las otras 3 sí siguen corriendo)
        if (LocalDate.now().getDayOfMonth() != DIA_FACTURACION_MES) return;

        CrudDAO<Contrato> contratoDAO = FactoryDAO.getDAO(Contrato.class);
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);
        CrudDAO<Empresa> empresaDAO = FactoryDAO.getDAO(Empresa.class);
        CrudDAO<Abono> abonoDAO = FactoryDAO.getDAO(Abono.class);

        ContratoService contratoService = new ContratoService(contratoDAO);
        FacturacionService facturacionService = new FacturacionService(facturaDAO, abonoDAO);
        EmpresaService empresaService = new EmpresaService(empresaDAO, List.of(new NotificadorService()));

        YearMonth mesActual = YearMonth.now();

        for (Contrato contrato : contratoService.buscarActivos()) {
            boolean yaFacturadoEsteMes = facturacionService.buscarPorContrato(contrato.getId()).stream()
                    .anyMatch(f -> YearMonth.from(f.getFechaEmision()).equals(mesActual));

            if (!yaFacturadoEsteMes) {
                double consumo = obtenerConsumoDelPeriodo(contrato);
                Factura factura = facturacionService.emitirFactura(contrato, consumo);

                empresaService.buscarPorId(contrato.getServicio().getEmpresa().getId())
                        .ifPresent(empresa -> empresa.emitirFactura(factura));
            }
        }
    }

    private double obtenerConsumoDelPeriodo(Contrato contrato) {
        return switch (contrato.getServicio().getTipo()) {
            case AGUA, LUZ -> ThreadLocalRandom.current().nextInt(15, 26); // 15 a 25 inclusive
            case BASURA, INTERNET -> 1.0;
        };
    }

    //2) APLICAR MORA
    private void aplicarMoraAVencidas() {
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);
        CrudDAO<InteresMora> interesDAO = FactoryDAO.getDAO(InteresMora.class);
        CrudDAO<Abono> abonoDAO = FactoryDAO.getDAO(Abono.class);
        FacturacionService facturacionService = new FacturacionService(facturaDAO, abonoDAO);
        MoraService moraService = new MoraService(interesDAO, facturaDAO, facturacionService);

        assert facturaDAO != null;
        facturaDAO.findAll().stream()
                .filter(f -> f.getEstado() != EstadoFactura.PAGADA) // antes: solo PENDIENTE
                .filter(Factura::estaVencida)
                .forEach(moraService::aplicarMora);
    }

    //3) CORTAR SERVICIOS
    private void cortarServiciosMorosos() {
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);
        CrudDAO<CorteServicio> corteDAO = FactoryDAO.getDAO(CorteServicio.class);
        CorteService corteService = new CorteService(corteDAO,facturaDAO);

        assert facturaDAO != null;
        List<Factura> candidatas = facturaDAO.findAll().stream()
                .filter(f -> f.getEstado() != EstadoFactura.PAGADA)
                .filter(Factura::superaFechaCorte)
                .toList();

        for (Factura factura : candidatas) {
            Contrato contrato = factura.getContrato();
            Optional<CorteServicio> corteVigente = corteService.buscarCorteVigente(contrato.getId());
            if (corteVigente.isEmpty()) corteService.cortarServicio(contrato, factura);
        }
    }

    //4) TERMINAR CONTRATOS CON CORTE PROLONGADO
    private void terminarContratosPorCorteProlongado() {
        CrudDAO<Contrato> contratoDAO = FactoryDAO.getDAO(Contrato.class);
        CrudDAO<CorteServicio> corteDAO = FactoryDAO.getDAO(CorteServicio.class);
        CrudDAO<Factura> facturaDAO = FactoryDAO.getDAO(Factura.class);

        ContratoService contratoService = new ContratoService(contratoDAO);
        CorteService corteService = new CorteService(corteDAO,facturaDAO);

        for (CorteServicio corte : corteService.buscarCortados()) {
            long diasCortado = ChronoUnit.DAYS.between(corte.getFechaCorte(), LocalDateTime.now());
            if (diasCortado >= DIAS_CORTADO_PARA_TERMINAR) {
                contratoService.terminarContrato(corte.getContrato(), CausaTerminacion.EMPRESA);
                assert corteDAO != null;
                corteDAO.delete(String.valueOf(corte.getId()));
            }
        }
    }
}