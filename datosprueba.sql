INSERT INTO Clientes (cedula, nombre, celular, email, activo) VALUES
    ('0102030405', 'Carlos Mendoza', '0991234567', 'carlos@mail.com', 1),
    ('0908070605', 'Ana Lopez', '0987654321', 'ana@mail.com', 1),
    ('1122334455', 'Luis Fernandez', '0971122334', 'luis@mail.com', 1);

INSERT INTO Empresa (nombre) VALUES
    ('ETAPA'),
    ('CentroSur'),
    ('EMAC'),
    ('FibraMax');

INSERT INTO Servicio (servicio, tipoValor, costoReactivacion, tasaInteresDiario, idEmpresa) VALUES
    ('Agua Potable', 'Variable', 15.00, 0.0100, 1),
    ('Electricidad', 'Variable', 25.00, 0.0150, 2),
    ('Recolección de Basura', 'Fijo', 5.00, 0.0050, 3),
    ('Internet', 'Fijo', 10.00, 0.0200, 4);

INSERT INTO Contrato (fechaInicio, fechaFin, causaTerminacion, fechaTerminacion, idServicios, idCliente) VALUES
    ('2025-01-15', NULL, 0, NULL, 1, '0102030405'),
    ('2025-02-01', NULL, 0, NULL, 4, '0102030405'),
    ('2024-11-10', NULL, 0, NULL, 2, '0908070605');

INSERT INTO Factura (fechaEmision, fechaVencimiento, fechaCorte, valorTotal, estado, idContrato) VALUES
    ('2026-06-01', '2026-06-15', '2026-06-20', 22.50, 'PENDIENTE', 1),
    ('2026-06-01', '2026-06-10', '2026-06-15', 35.00, 'PENDIENTE', 2),
    ('2026-05-01', '2026-05-15', '2026-05-20', 42.80, 'CORTADA', 3);

INSERT INTO ModalidadPago (modalidad) VALUES
    ('Tarjeta de Crédito'),
    ('Tarjeta de Débito'),
    ('Transferencia Bancaria'),
    ('PayPal');

INSERT INTO Abono (monto, fechaPago, pagoRealizado, idModalidad, idFactura) VALUES
    (10.00, '2026-06-05 10:30:00', 1, 3, 1),
    (35.00, '2026-06-08 14:15:00', 0, 1, 2);

INSERT INTO InteresMora (diasRetraso, interesAcumulado, fechaCalculo, aplicadoAFactura, idFactura) VALUES
    (18, 1.25, '2026-06-07', 1, 3);

INSERT INTO CorteServicio (fechaCorte, fechaReactivacion, costoReactivacionPagado, estadoCorte, idContrato, idFactura) VALUES
    ('2026-05-21', NULL, NULL, 'VIGENTE', 3, 3);

INSERT INTO Carrito (montoAbono, idCliente, idAbono) VALUES
    (35.00, '0102030405', 2);