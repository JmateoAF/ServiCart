INSERT INTO Clientes (cedula, nombre, email, celular, activo) VALUES
    ('0102030405', 'Carlos Mendoza', 'carlos@mail.com', '0991234567', 1),
    ('0908070605', 'Ana Lopez', 'ana@mail.com', '0987654321', 1),
    ('1122334455', 'Luis Fernandez',  'luis@mail.com', '0971122334', 1);

INSERT INTO Servicio (tipoServicio, tarifaFija, tarifaPorUnidad, tipoValor, costoReactivacion, tasaInteresDiario, idEmpresa) VALUES
    (0, 10.50, 0.00, 0, 15.00, 0.0100, 0),
    (1, 0.00, 0.15, 1, 25.00, 0.0150, 1),
    (2, 5.00, 0.00, 0, 5.00, 0.0050, 2),
    (3, 20.00, 0.00, 0, 10.00, 0.0200, 3);

INSERT INTO Contrato (fechaInicio, fechaFin, causaTerminacion, fechaTerminacion, idServicios, idCliente) VALUES
    ('2025-01-15 08:00:00', NULL, 0, NULL, 1, '0102030405'),
    ('2025-02-01 10:30:00', NULL, 0, NULL, 4, '0102030405'),
    ('2024-11-10 14:15:00', NULL, 0, NULL, 2, '0908070605');

INSERT INTO Factura (fechaEmision, fechaVencimiento, fechaCorte, valorTotal, estado, idContrato) VALUES
    ('2026-06-01 00:00:00', '2026-06-15 23:59:59', '2026-06-20 00:00:00', 22.50, 0, 1),
    ('2026-06-01 00:00:00', '2026-06-10 23:59:59', '2026-06-15 00:00:00', 35.00, 0, 2),
    ('2026-05-01 00:00:00', '2026-05-15 23:59:59', '2026-05-20 00:00:00', 42.80, 1, 3);

INSERT INTO Abono (monto, fechaPago, pagoRealizado, idFactura, idModalidad) VALUES
    (10.00, '2026-06-05 10:30:00', 1, 1, 3),
    (35.00, '2026-06-08 14:15:00', 0, 2, 1);

INSERT INTO InteresMora (diasRetraso, interesAcumulado, fechaCalculo, aplicadoAFactura, idFactura) VALUES
    (18, 1.25, '2026-06-07 00:00:00', 1, 3);

INSERT INTO CorteServicio (fechaCorte, fechaReactivacion, costoReactivacionPagado, estadoCorte, idContrato, idFactura) VALUES
    ('2026-05-21 12:00:00', NULL, NULL, 0, 3, 3);

INSERT INTO Carrito (montoAbono, idCliente, idAbono) VALUES
    (35.00, '0102030405', 2);

INSERT INTO Administradores (usuario, contrasenia) VALUES
    ('admin', 'pass1');