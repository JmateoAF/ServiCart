--CREACIÓN DE TABLAS

-- Habilitar el soporte de claves foráneas en SQLite
-- PRAGMA foreign_keys = ON;

-- 1. Empresas que proveen servicios (ETAPA, CentroSur, etc.) [cite: 4]
CREATE TABLE IF NOT EXISTS empresas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    tipo_servicio TEXT NOT NULL -- Ej: Agua, Electricidad, Basura, Internet
);

-- 2. Clientes del sistema
CREATE TABLE IF NOT EXISTS clientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL
);

-- 3. Contratos entre cliente y empresa [cite: 5]
CREATE TABLE IF NOT EXISTS contratos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cliente_id INTEGER NOT NULL,
    empresa_id INTEGER NOT NULL,
    fecha_inicio DATE NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (empresa_id) REFERENCES empresas(id)
);

-- 4. Facturas emitidas [cite: 6]
CREATE TABLE IF NOT EXISTS facturas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    contrato_id INTEGER NOT NULL,
    valor_total REAL NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL, -- [cite: 12]
    fecha_corte DATE NOT NULL,       -- [cite: 13]
    estado TEXT DEFAULT 'PENDIENTE', -- Pendiente, Pagada, Cortada
    FOREIGN KEY (contrato_id) REFERENCES contratos(id)
);

-- 5. Abonos (Pagos parciales o totales) [cite: 9, 10]
CREATE TABLE IF NOT EXISTS abonos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    factura_id INTEGER NOT NULL,
    monto REAL NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    metodo_pago TEXT NOT NULL, -- [cite: 11]
    FOREIGN KEY (factura_id) REFERENCES facturas(id)
);

-- 6. Carrito de Compras (Gestión de abonos pendientes)
CREATE TABLE IF NOT EXISTS carrito (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cliente_id INTEGER NOT NULL,
    factura_id INTEGER NOT NULL,
    monto_abono REAL NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (factura_id) REFERENCES facturas(id)
);