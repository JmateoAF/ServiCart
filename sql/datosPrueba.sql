PRAGMA foreign_keys = ON;

-- Datos mínimos indispensables: solo lo que la app no puede generar por sí sola
-- (credenciales, catálogo, clientes y sus contratos/facturas). Abonos, intereses de
-- mora y cortes de servicio NO se siembran aquí: el propio programa los calcula
-- (MoraService, CorteService, GestionAutomaticaEmpresaJob) apenas arranca, a partir
-- de las fechas de las facturas sembradas abajo.

INSERT OR IGNORE INTO Administradores(usuario, contrasenia)
VALUES ('admin', 'admin123');

INSERT OR IGNORE INTO Clientes(cedula, nombre, email, celular, activo) VALUES
    ('0106807365', 'Jostin Aucancela', 'jmaf@mail.com', '0963304126', 1),
    ('0107758666', 'Maritza Quishpi', 'bmqc@mail.com', '0987545680', 1),
    ('0107645095', 'Mateo Capelo', 'cmcc@mail.com', '0983970282', 0);

INSERT OR IGNORE INTO Empresa(nombre) VALUES
    ('ETAPA'),
    ('CENTROSUR'),
    ('EMAC'),
    ('FIBRAMAX');

-- Orden: 1=ETAPA/AGUA (variable), 2=CENTROSUR/LUZ (variable), 3=EMAC/BASURA (fijo), 4=FIBRAMAX/INTERNET (fijo)
INSERT OR IGNORE INTO ServicioCatalogo(idEmpresa, tipoServicio, tipoValor, tarifaFija, tarifaPorUnidad, costoReactivacion, tasaInteresDiario) VALUES
    (1, 0, 1, 0.00, 0.85, 10.00, 0.0500),
    (2, 1, 1, 0.00, 1.80, 10.00, 0.0300),
    (3, 2, 0, 12.00, 0.00, 5.00, 0.0200),
    (4, 3, 0, 35.00, 0.00, 15.00, 0.0400);

-- Contrato1 (LUZ) y Contrato2 (INTERNET) para el cliente principal de la demo;
-- Contrato3 (AGUA) para el segundo cliente; Contrato4 (BASURA) ya terminado,
-- perteneciente al cliente inactivo (para poder demostrar la reactivación de usuarios).
INSERT OR IGNORE INTO Contrato(fechaInicio, fechaFin, causaTerminacion, idServicio, idCliente) VALUES
    ('2025-01-10 09:00:00', NULL, 0, 2, '0106807365'),
    ('2025-03-05 14:30:00', NULL, 0, 4, '0106807365'),
    ('2024-11-20 10:00:00', NULL, 0, 1, '0107758666'),
    ('2024-01-15 10:00:00', '2026-05-01 00:00:00', 1, 3, '0107645095');

-- Facturas en tres estados distintos, para poder ver de inmediato (sin esperar
-- al ciclo automático diario) los tres caminos del negocio:
--   1) pendiente y aún no vencida        -> se puede agregar al carrito y pagar
--   2) ya vencida pero sin corte todavía -> MoraService le calculará el interés al iniciar la app
--   3) ya pasó su fecha de corte         -> CorteService la cortará automáticamente al iniciar la app
-- Todas nacen en estado 0 (PENDIENTE), igual que las crea FacturacionService.emitirFactura();
-- es la propia gestión automática la que las hace avanzar a VENCIDA/cortada.
INSERT OR IGNORE INTO Factura(fechaEmision, fechaVencimiento, fechaCorte, valorBase, valorTotal, estado, idContrato) VALUES
    ('2026-07-01 00:00:00', '2026-07-31 23:59:59', '2026-08-15 00:00:00', 46.80, 46.80, 0, 1),
    ('2026-06-10 00:00:00', '2026-07-10 23:59:59', '2026-07-25 00:00:00', 35.00, 35.00, 0, 2),
    ('2026-05-01 00:00:00', '2026-05-31 23:59:59', '2026-06-15 00:00:00', 21.25, 21.25, 0, 3);
