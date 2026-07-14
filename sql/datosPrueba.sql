PRAGMA foreign_keys = ON;

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

INSERT OR IGNORE INTO ServicioCatalogo(idEmpresa, tipoServicio, tipoValor, tarifaFija, tarifaPorUnidad, costoReactivacion, tasaInteresDiario, diasParaCorte) VALUES
    (1, 0, 1, 0.00, 0.85, 10.00, 0.0500, 15),
    (2, 1, 1, 0.00, 1.80, 10.00, 0.0300, 15),
    (3, 2, 0, 12.00, 0.00, 5.00, 0.0200, 15),
    (4, 3, 0, 35.00, 0.00, 15.00, 0.0400, 15);

INSERT OR IGNORE INTO Contrato(fechaInicio, fechaFin, causaTerminacion, idServicio, idCliente) VALUES
    ('2025-01-10 09:00:00', NULL, 0, 2, '0106807365'),
    ('2025-03-05 14:30:00', NULL, 0, 4, '0106807365'),
    ('2024-11-20 10:00:00', NULL, 0, 1, '0107758666'),
    ('2024-01-15 10:00:00', '2026-05-01 00:00:00', 1, 3, '0107645095');

INSERT OR IGNORE INTO Factura(fechaEmision, fechaVencimiento, fechaCorte, valorBase, valorTotal, estado, idContrato) VALUES
    ('2026-07-11 00:00:00', '2026-07-18 00:00:00', '2026-08-02 00:00:00', 46.80, 46.80, 0, 1),
    ('2026-06-25 00:00:00', '2026-07-02 00:00:00', '2026-07-17 00:00:00', 35.00, 35.00, 0, 2),
    ('2026-06-14 00:00:00', '2026-06-21 00:00:00', '2026-07-06 00:00:00', 21.25, 21.25, 0, 3);
