PRAGMA foreign_keys = ON;

INSERT OR IGNORE INTO Administradores(usuario, contrasenia)
VALUES ('admin', 'admin123');

INSERT OR IGNORE INTO Clientes(cedula, nombre, email, celular, activo) VALUES
    ('0106807365', 'Jostin Aucancela', 'jmaf@mail.com', '0963304126', 1),
    ('0107758666', 'Maritza Quishpi', 'bmqc@mail.com', '0987545680', 1),
    ('0107645095', 'Mateo Capelo', 'cmcc@mail.com', '0983970282', 0);

INSERT OR IGNORE INTO Empresa(nombre)VALUES
    ('ETAPA'),
    ('CENTROSUR'),
    ('EMAC'),
    ('FIBRAMAX');

INSERT OR IGNORE INTO ServicioCatalogo(idEmpresa, tipoServicio, tipoValor, tarifaFija, tarifaPorUnidad, costoReactivacion, tasaInteresDiario) VALUES
    (1, 0, 1, 0.00, 0.85, 10.00, 0.0500),
    (2, 1, 1, 0.00, 1.80, 10.00, 0.0300),
    (3, 2, 0, 12.00, 0.00, 5.00, 0.0200),
    (4, 3, 0, 35.00, 0.00, 15.00, 0.0400);

INSERT OR IGNORE INTO Contrato(fechaInicio, fechaFin, causaTerminacion, idServicio, idCliente) VALUES
    ('2024-03-15 09:00:00', NULL, 0, 2, '0106807365'),
    ('2025-01-10 14:30:00', NULL, 0, 4, '0106807365'),
    ('2024-06-20 10:00:00', NULL, 0, 1, '0107758666'),
    ('2025-02-05 11:20:00', NULL, 0, 3, '0107758666'),
    ('2024-11-10 14:15:00', '2026-06-05 00:00:00', 1, 1, '0107645095'),
    ('2024-08-22 08:45:00', NULL, 0, 2, '0107758666');

INSERT OR IGNORE INTO Factura(fechaEmision, fechaVencimiento, fechaCorte, valorBase, valorTotal, estado, idContrato) VALUES
     ('2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-15 00:00:00', 54.30, 54.30, 0, 1),
     ('2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-15 00:00:00', 35.00, 35.00, 1, 2),
     ('2026-05-01 00:00:00', '2026-05-31 23:59:59', '2026-06-15 00:00:00', 24.50, 24.50, 2, 3),
     ('2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-15 00:00:00', 12.00, 12.00, 0, 4),
     ('2026-05-01 00:00:00', '2026-05-31 23:59:59', '2026-06-05 00:00:00', 18.60, 18.60, 1, 5),
     ('2026-04-01 00:00:00', '2026-04-30 23:59:59', '2026-05-15 00:00:00', 44.70, 44.70, 1, 1),
     ('2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-15 00:00:00', 55.20, 55.20, 2, 6);

-- Sin abonos sembrados: sin pagos previos no hay riesgo de que una factura quede
-- matemáticamente saldada por unos abonos de prueba pero con estado desalineado.

INSERT OR IGNORE INTO InteresMora(diasRetraso, interesAcumulado, fechaCalculo, aplicadoAFactura, idFactura) VALUES
    (14, 12.25, '2026-06-29 00:00:00', 0, 3),
    (8, 8.85, '2026-06-28 00:00:00', 0, 7);

INSERT OR IGNORE INTO CorteServicio(fechaCorte, fechaReactivacion, costoReactivacionPagado, estadoCorte, idContrato, idFactura) VALUES
    ('2026-06-06 08:00:00', NULL, 0.0, 1, 5, 5);

INSERT OR IGNORE INTO Carrito(idCliente) VALUES
    ('0106807365'),
    ('0107758666'),
    ('0107645095');

-- Carritos vacíos: sin abonos sembrados no hay nada que agregarles