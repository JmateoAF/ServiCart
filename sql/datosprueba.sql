PRAGMA foreign_keys = ON;

INSERT OR IGNORE INTO Administradores(usuario, contrasenia) VALUES
    ('admin', 'admin123');

INSERT OR IGNORE INTO Clientes(cedula, nombre, email, celular, activo) VALUES
    ('0102030405', 'Carlos Mendoza', 'carlos@mail.com', '0991234567', 1),
    ('0908070605', 'Ana Lopez', 'ana@mail.com', '0987654321', 1),
    ('1122334455', 'Luis Fernandez', 'luis@mail.com', '0971122334', 1);

INSERT OR IGNORE INTO ServicioCatalogo(id, empresa, tipoServicio, tipoValor, tarifaFija, tarifaPorUnidad, costoReactivacion, tasaInteresDiario) VALUES
    (1,  0, 0, 1,  0.00, 0.85, 50.00, 0.0500),
    (2,  1, 1, 1,  0.00, 1.80, 80.00, 0.0300),
    (3,  2, 2, 0, 12.00, 0.00, 30.00, 0.0200),
    (4,  3, 3, 0, 35.00, 0.00, 60.00, 0.0400);

INSERT OR IGNORE INTO Contrato(id, fechaInicio, fechaFin, causaTerminacion, idServicio, idCliente) VALUES
    (1, '2025-01-15 08:00:00', NULL, 0, 1, '0102030405'),
    (2, '2025-02-01 10:30:00', NULL, 0, 4, '0102030405'),
    (3, '2024-11-10 14:15:00', NULL, 0, 2, '0908070605');

INSERT OR IGNORE INTO Factura(id, fechaEmision, fechaVencimiento, fechaCorte, valorTotal, estado, idContrato) VALUES
    (1, '2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-10 00:00:00', 12.75, 0, 1),
    (2, '2026-06-01 00:00:00', '2026-06-10 23:59:59', '2026-06-20 00:00:00', 35.00, 0, 2),
    (3, '2026-05-01 00:00:00', '2026-05-31 23:59:59', '2026-06-05 00:00:00', 42.80, 1, 3);

INSERT OR IGNORE INTO Abono(id, monto, fechaPago, pagoRealizado, modalidadPago, idFactura) VALUES
    (1, 10.00, '2026-06-05 10:30:00', 1, 3, 1),
    (2, 35.00, '2026-06-26 14:00:00', 0, 0, 2),
    (3, 42.80, '2026-05-20 09:00:00', 1, 1, 3);

INSERT OR IGNORE INTO InteresMora(id, diasRetraso, interesAcumulado, fechaCalculo, aplicadoAFactura, idFactura) VALUES
    (1, 16, 22.40, '2026-06-26 00:00:00', 0, 2);

INSERT OR IGNORE INTO CorteServicio(id, fechaCorte, fechaReactivacion, costoReactivacionPagado, estadoCorte, idContrato, idFactura) VALUES
    (1, '2026-06-06 08:00:00', NULL, 0.0, 1, 3, 3);

INSERT OR IGNORE INTO Carrito (id, idCliente) VALUES
    (1, '0102030405');

INSERT OR IGNORE INTO CarritoAbono (idCarrito, idAbono) VALUES
    (1, 2);
