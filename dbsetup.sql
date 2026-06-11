CREATE TABLE IF NOT EXISTS Clientes (
    cedula TEXT NOT NULL UNIQUE PRIMARY KEY,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    celular TEXT NOT NULL UNIQUE,
    activo INTEGER
);

CREATE TABLE IF NOT EXISTS Empresa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Servicio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    servicio TEXT NOT NULL,
    tipoValor TEXT NOT NULL,
    costoReactivacion DECIMAL NOT NULL,
    tasaInteresDiario DECIMAL NOT NULL DEFAULT 0.15,
    idEmpresa INTEGER NOT NULL,
    CONSTRAINT fkServicio FOREIGN KEY (idEmpresa) REFERENCES Empresa(id)
);

CREATE TABLE IF NOT EXISTS Contrato (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaInicio DATE NOT NULL,
    fechaFin DATE,
    causaTerminacion INTEGER DEFAULT 0, --0 no termina, 1 empresa, 2 cliente
    fechaTerminacion DATE,
    idServicios INTEGER NOT NULL,
    idCliente TEXT NOT NULL,
    CONSTRAINT fkContratosCliente FOREIGN KEY (idCliente) REFERENCES clientes(cedula),
    CONSTRAINT fkContratosServicios FOREIGN KEY (idServicios) REFERENCES Servicio(id)
);

CREATE TABLE IF NOT EXISTS Factura (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaEmision DATE NOT NULL,
    fechaVencimiento DATE NOT NULL,
    fechaCorte DATE NOT NULL,
    valorTotal DECIMAL NOT NULL,
    estado TEXT DEFAULT 'PENDIENTE',
    idContrato INTEGER NOT NULL,
    CONSTRAINT fkFactura FOREIGN KEY (idContrato) REFERENCES Contrato(id)
);

CREATE TABLE IF NOT EXISTS ModalidadPago (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    modalidad TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Abono (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    monto DECIMAL NOT NULL,
    fechaPago DATETIME NOT NULL,
    pagoRealizado BOOL DEFAULT FALSE,
    idFactura INTEGER NOT NULL,
    idModalidad TEXT NOT NULL,
    CONSTRAINT fkAbonoFactura FOREIGN KEY (idFactura) REFERENCES Factura(id),
    CONSTRAINT fkAbonoModalidad FOREIGN KEY (idModalidad) REFERENCES ModalidadPago(id)
);

CREATE TABLE IF NOT EXISTS InteresMora (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    diasRetraso INTEGER NOT NULL,
    interesAcumulado DECIMAL NOT NULL,
    fechaCalculo DATE NOT NULL,
    aplicadoAFactura BOOLEAN DEFAULT FALSE,
    idFactura INTEGER NOT NULL,
    CONSTRAINT fkInteresMora FOREIGN KEY (idFactura) REFERENCES Factura(id)
);

CREATE TABLE IF NOT EXISTS CorteServicio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaCorte DATE NOT NULL,
    fechaReactivacion DATE,
    costoReactivacionPagado DECIMAL,
    estadoCorte TEXT,
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
    CONSTRAINT fkCarritoClientes FOREIGN KEY (idCliente) REFERENCES clientes(cedula),
    CONSTRAINT fkCarritoAbono FOREIGN KEY (idAbono) REFERENCES Abono(id)
);