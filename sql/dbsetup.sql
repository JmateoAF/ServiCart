PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS Clientes (
    cedula TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    celular TEXT NOT NULL UNIQUE,
    activo INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS Administradores (
    usuario TEXT PRIMARY KEY,
    contrasenia TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Empresa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS ServicioCatalogo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idEmpresa INTEGER NOT NULL,
    tipoServicio INTEGER NOT NULL,
    tipoValor INTEGER NOT NULL,
    tarifaFija REAL NOT NULL DEFAULT 0.0,
    tarifaPorUnidad REAL NOT NULL DEFAULT 0.0,
    costoReactivacion REAL NOT NULL,
    tasaInteresDiario REAL NOT NULL DEFAULT 0.01,
    CONSTRAINT fkEmpresa FOREIGN KEY (idEmpresa) REFERENCES Empresa(id)
);

CREATE TABLE IF NOT EXISTS Contrato (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaInicio DATETIME NOT NULL,
    fechaFin DATETIME,
    causaTerminacion INTEGER NOT NULL DEFAULT 0,
    idServicio INTEGER NOT NULL,
    idCliente TEXT NOT NULL,
    CONSTRAINT fkContratoServicio FOREIGN KEY (idServicio) REFERENCES ServicioCatalogo(id),
    CONSTRAINT fkContratoCliente FOREIGN KEY (idCliente) REFERENCES Clientes(cedula)
);

CREATE TABLE IF NOT EXISTS Factura (
    id INTEGER  PRIMARY KEY AUTOINCREMENT,
    fechaEmision DATETIME NOT NULL,
    fechaVencimiento DATETIME NOT NULL,
    fechaCorte DATETIME NOT NULL,
    valorTotal REAL NOT NULL,
    estado INTEGER NOT NULL DEFAULT 0,
    idContrato INTEGER NOT NULL,
    CONSTRAINT fkFacturaContrato FOREIGN KEY (idContrato) REFERENCES Contrato(id)
);

CREATE TABLE IF NOT EXISTS Abono (
    id INTEGER  PRIMARY KEY AUTOINCREMENT,
    monto REAL NOT NULL,
    fechaPago DATETIME NOT NULL,
    pagoRealizado INTEGER NOT NULL DEFAULT 0,
    modalidadPago INTEGER NOT NULL,
    idFactura INTEGER  NOT NULL,
    CONSTRAINT fkAbonoFactura FOREIGN KEY (idFactura) REFERENCES Factura(id)
);

CREATE TABLE IF NOT EXISTS InteresMora (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    diasRetraso INTEGER NOT NULL,
    interesAcumulado REAL NOT NULL,
    fechaCalculo DATETIME NOT NULL,
    aplicadoAFactura INTEGER NOT NULL DEFAULT 0,
    idFactura INTEGER NOT NULL,
    CONSTRAINT fkInteresMoraFactura FOREIGN KEY (idFactura) REFERENCES Factura(id)
);

CREATE TABLE IF NOT EXISTS CorteServicio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaCorte DATETIME NOT NULL,
    fechaReactivacion DATETIME,
    costoReactivacionPagado REAL NOT NULL DEFAULT 0.0,
    estadoCorte INTEGER  NOT NULL DEFAULT 1,
    idContrato INTEGER  NOT NULL,
    idFactura INTEGER  NOT NULL,
    CONSTRAINT fkCorteContrato FOREIGN KEY (idContrato) REFERENCES Contrato(id),
    CONSTRAINT fkCorteFactura  FOREIGN KEY (idFactura) REFERENCES Factura(id)
);

CREATE TABLE IF NOT EXISTS Carrito (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idCliente TEXT NOT NULL UNIQUE,
    CONSTRAINT fkCarritoCliente FOREIGN KEY (idCliente) REFERENCES Clientes(cedula)
);

CREATE TABLE IF NOT EXISTS CarritoAbono (
    idCarrito INTEGER NOT NULL,
    idAbono INTEGER NOT NULL,
    PRIMARY KEY (idCarrito, idAbono),
    CONSTRAINT fkCarritoAbonoCarrito FOREIGN KEY (idCarrito) REFERENCES Carrito(id),
    CONSTRAINT fkCarritoAbonoAbono   FOREIGN KEY (idAbono) REFERENCES Abono(id)
);