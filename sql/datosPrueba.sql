PRAGMA foreign_keys = ON;

INSERT OR IGNORE INTO Administradores(usuario, contrasenia)
VALUES ('admin', 'admin123');

INSERT OR IGNORE INTO Empresa(nombre)VALUES
    ('ETAPA'),
    ('CENTROSUR'),
    ('EMAC'),
    ('FIBRAMAX');

INSERT OR IGNORE INTO Clientes(cedula, nombre, email, celular, activo) VALUES
    ('0102030405', 'Carlos Mendoza', 'carlos@mail.com', '0991234567', 1),
    ('0908070605', 'Ana Lopez', 'ana@mail.com', '0987654321', 1),
    ('1122334455', 'Luis Fernandez', 'luis@mail.com', '0971122334', 1);

INSERT OR IGNORE INTO ServicioCatalogo(idEmpresa, tipoServicio, tipoValor, tarifaFija, tarifaPorUnidad, costoReactivacion, tasaInteresDiario) VALUES
    (1, 0, 1, 0.00, 0.85, 50.00, 0.0500),
    (1, 0, 1, 0.00, 0.90, 50.00, 0.0500),
    (2, 1, 1, 0.00, 1.80, 80.00, 0.0300),
    (2, 1, 1, 0.00, 1.85, 80.00, 0.0300),
    (2, 1, 1, 0.00, 1.75, 80.00, 0.0300),
    (3, 2, 0, 12.00, 0.00, 30.00, 0.0200),
    (3, 2, 0, 12.50, 0.00, 30.00, 0.0200),
    (3, 2, 0, 13.00, 0.00, 30.00, 0.0200),
    (4, 3, 0, 35.00, 0.00, 60.00, 0.0400),
    (4, 3, 0, 45.00, 0.00, 60.00, 0.0400);

INSERT OR IGNORE INTO Contrato(fechaInicio, fechaFin, causaTerminacion, idServicio, idCliente) VALUES
    ('2024-03-15 09:00:00', NULL, 0, 3, '0106807365'),
    ('2025-01-10 14:30:00', NULL, 0, 9, '0106807365'),
    ('2024-06-20 10:00:00', NULL, 0, 1, '0107758666'),
    ('2025-02-05 11:20:00', NULL, 0, 6, '0107758666'),
    ('2024-11-10 14:15:00', '2026-06-05 00:00:00', 1, 2, '0107645095'),
    ('2024-08-22 08:45:00', NULL, 0, 4, '0107758666');

INSERT OR IGNORE INTO Factura(fechaEmision, fechaVencimiento, fechaCorte, valorTotal, estado, idContrato) VALUES
    ('2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-15 00:00:00', 54.30, 0, 1),
    ('2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-15 00:00:00', 35.00, 1, 2),
    ('2026-05-01 00:00:00', '2026-05-31 23:59:59', '2026-06-15 00:00:00', 24.50, 2, 3),
    ('2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-15 00:00:00', 12.00, 0, 4),
    ('2026-05-01 00:00:00', '2026-05-31 23:59:59', '2026-06-05 00:00:00', 18.60, 1, 5),
    ('2026-04-01 00:00:00', '2026-04-30 23:59:59', '2026-05-15 00:00:00', 44.70, 1, 1),
    ('2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-15 00:00:00', 55.20, 2, 6);

INSERT OR IGNORE INTO Abono(monto, fechaPago, pagoRealizado, modalidadPago, idFactura) VALUES
    (30.00, '2026-06-15 10:30:00', 1, 0, 1),
    (24.30, '2026-06-28 09:45:00', 1, 3, 1),
    (35.00, '2026-06-20 14:00:00', 1, 3, 2),
    (24.50, '2026-06-10 09:45:00', 0, 1, 3),
    (12.00, '2026-06-28 11:15:00', 0, 2, 4),
    (18.60, '2026-05-20 09:00:00', 1, 4, 5),
    (44.70, '2026-04-25 13:00:00', 1, 0, 6),
    (55.20, '2026-06-25 16:20:00', 0, 4, 7);

INSERT OR IGNORE INTO InteresMora(diasRetraso, interesAcumulado, fechaCalculo, aplicadoAFactura, idFactura) VALUES
    (14, 12.25, '2026-06-29 00:00:00', 0, 3),
    (8, 8.85, '2026-06-28 00:00:00', 0, 7);

INSERT OR IGNORE INTO CorteServicio(fechaCorte, fechaReactivacion, costoReactivacionPagado, estadoCorte, idContrato, idFactura) VALUES
    ('2026-06-06 08:00:00', NULL, 0.0, 1, 5, 5);

INSERT OR IGNORE INTO Carrito(idCliente) VALUES
    ('0106807365'),
    ('0107758666'),
    ('0107645095');

INSERT OR IGNORE INTO CarritoAbono(idCarrito, idAbono) VALUES
    (1, 1),
    (1, 2),
    (2, 4),
    (2, 5),
    (3, 8);