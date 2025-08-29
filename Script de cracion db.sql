-- Crear Base de Datos
CREATE DATABASE EmpresaSeguridad;
USE EmpresaSeguridad;

-- ==========================
-- TABLAS PRINCIPALES
-- ==========================

-- Tabla de Usuarios
CREATE TABLE Usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL  -- admin, empleado, cliente
);

-- Tabla de Clientes
CREATE TABLE Cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    telefono VARCHAR(20),
    correo VARCHAR(100),
    idUsuario INT,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- Tabla de Planes de Seguridad
CREATE TABLE PlanSeguridad (
    idPlan INT AUTO_INCREMENT PRIMARY KEY,
    tipoPlan VARCHAR(50) NOT NULL,
    descripcion TEXT,
    costoMensual DECIMAL(10,2) NOT NULL
);

-- Tabla de Facturas
CREATE TABLE Factura (
    idFactura INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    montoTotal DECIMAL(10,2) NOT NULL,
    idCliente INT,
    idPlan INT,
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idPlan) REFERENCES PlanSeguridad(idPlan)
);

-- Relación Cliente - PlanSeguridad (N:M)
CREATE TABLE Cliente_Plan (
    idCliente INT,
    idPlan INT,
    fechaInicio DATE,
    fechaFin DATE,
    PRIMARY KEY (idCliente, idPlan),
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idPlan) REFERENCES PlanSeguridad(idPlan)
);

-- ==========================
-- SEGURIDAD: ROLES Y PERMISOS
-- ==========================

-- Tabla de Roles
CREATE TABLE Rol (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombreRol VARCHAR(50) UNIQUE NOT NULL
);

-- Tabla de Permisos
CREATE TABLE Permiso (
    idPermiso INT AUTO_INCREMENT PRIMARY KEY,
    nombrePermiso VARCHAR(100) UNIQUE NOT NULL
);

-- Tabla intermedia Rol - Permisos
CREATE TABLE Rol_Permiso (
    idRol INT,
    idPermiso INT,
    PRIMARY KEY (idRol, idPermiso),
    FOREIGN KEY (idRol) REFERENCES Rol(idRol),
    FOREIGN KEY (idPermiso) REFERENCES Permiso(idPermiso)
);

-- Relación Usuario - Rol
CREATE TABLE Usuario_Rol (
    idUsuario INT,
    idRol INT,
    PRIMARY KEY (idUsuario, idRol),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idRol) REFERENCES Rol(idRol)
);

-- ==========================
-- TABLA DE LOGS
-- ==========================

CREATE TABLE LogSistema (
    idLog INT AUTO_INCREMENT PRIMARY KEY,
    idUsuario INT,
    accion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- ==========================
-- DATOS INICIALES
-- ==========================

-- Roles iniciales
INSERT INTO Rol (nombreRol) VALUES ('Administrador'), ('Empleado'), ('Cliente');

-- Permisos iniciales
INSERT INTO Permiso (nombrePermiso) 
VALUES ('CREAR_USUARIO'), ('VER_USUARIO'), ('EDITAR_USUARIO'), ('ELIMINAR_USUARIO'),
       ('CREAR_FACTURA'), ('VER_FACTURA'), ('CREAR_PLAN'), ('VER_PLAN');

-- Asignar permisos al rol Administrador
INSERT INTO Rol_Permiso (idRol, idPermiso)
SELECT 1, idPermiso FROM Permiso;  -- Rol 1 = Administrador tiene todos los permisos