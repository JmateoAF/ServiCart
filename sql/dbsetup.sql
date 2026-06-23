CREATE TABLE IF NOT EXISTS Clientes (
    cedula TEXT NOT NULL UNIQUE PRIMARY KEY,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    celular TEXT NOT NULL UNIQUE,
    activo INTEGER
);

CREATE TABLE IF NOT EXISTS Servicio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipoServicio INTEGER NOT NULL,
    tarifaFija DECIMAL DEFAULT 0.00,
    tarifaPorUnidad DECIMAL DEFAULT 0.00,
    tipoValor INTEGER NOT NULL,
    costoReactivacion DECIMAL NOT NULL,
    tasaInteresDiario DECIMAL NOT NULL DEFAULT 0.15,
    idEmpresa INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS Contrato (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaInicio DATETIME NOT NULL,
    fechaFin DATETIME,
    causaTerminacion INTEGER DEFAULT 0,
    fechaTerminacion DATETIME,
    idCliente TEXT NOT NULL,
    idServicios INTEGER NOT NULL,
    CONSTRAINT fkContratosCliente FOREIGN KEY (idCliente) REFERENCES Clientes(cedula),
    CONSTRAINT fkContratosServicios FOREIGN KEY (idServicios) REFERENCES Servicio(id)
);

CREATE TABLE IF NOT EXISTS Factura (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaEmision DATETIME NOT NULL,
    fechaVencimiento DATETIME NOT NULL,
    fechaCorte DATETIME NOT NULL,
    valorTotal DECIMAL NOT NULL,
    estado INTEGER NOT NULL,
    idContrato INTEGER NOT NULL,
    CONSTRAINT fkFactura FOREIGN KEY (idContrato) REFERENCES Contrato(id)
);

CREATE TABLE IF NOT EXISTS Abono (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    monto DECIMAL NOT NULL,
    fechaPago DATETIME NOT NULL,
    pagoRealizado BOOL DEFAULT FALSE,
    idFactura INTEGER NOT NULL,
    idModalidad INTEGER NOT NULL,
    CONSTRAINT fkAbonoFactura FOREIGN KEY (idFactura) REFERENCES Factura(id)
);

CREATE TABLE IF NOT EXISTS InteresMora (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    diasRetraso INTEGER NOT NULL,
    interesAcumulado DECIMAL NOT NULL,
    fechaCalculo DATETIME NOT NULL,
    aplicadoAFactura BOOLEAN DEFAULT FALSE,
    idFactura INTEGER NOT NULL,
    CONSTRAINT fkInteresMora FOREIGN KEY (idFactura) REFERENCES Factura(id)
);

CREATE TABLE IF NOT EXISTS CorteServicio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaCorte DATETIME NOT NULL,
    fechaReactivacion DATETIME,
    costoReactivacionPagado DECIMAL,
    estadoCorte INTEGER NOT NULL,
    idContrato INTEGER NOT NULL,
    idFactura INTEGER NOT NULL,
    CONSTRAINT fkCorteServiciosContrato FOREIGN KEY (idContrato) REFERENCES Contrato(id),
    CONSTRAINT fkCorteServiciosFactura FOREIGN KEY (idFactura) REFERENCES Factura(id)
);

CREATE TABLE IF NOT EXISTS Carrito (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    montoAbono DECIMAL NOT NULL,
    idCliente TEXT NOT NULL,
    idAbono INTEGER NOT NULL,
    CONSTRAINT fkCarritoClientes FOREIGN KEY (idCliente) REFERENCES Clientes(cedula),
    CONSTRAINT fkCarritoAbono FOREIGN KEY (idAbono) REFERENCES Abono(id)
);