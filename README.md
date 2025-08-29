![Logo](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRR4YvFZALqhd-Kl91Fgssmje1h1gJU8rJIgg&s)

# PROGRAMACIÓN II — Entregable 1

## 📋 Detalles del entregable

-  Diagrama de clases del sistema  
-  Diagrama del modelo de base de datos  
-  Script de creación del modelo de base de datos  

---

## 🔗 Diagramas en línea

### Diagrama de Clases  
[![Ver Diagrama de Clases](https://img.shields.io/badge/Diagrama%20de%20Clases-0A66C2?style=for-the-badge&logo=mermaid&logoColor=white)](https://mermaid.live/edit#pako:eNqNU8Fu2zAM_RWD5ySI7SaOde2QS1CgwLbL4IsmMY4wWzRoGVib-qP2DfuxyU7d1VGAVieJ74l8fATPoEgjCED-YmTJsi5s5M_3tpNsKDpfnsMx1kVGT8Dj4T_SOja2jCzVPxmDsCJmpBth61i2-PePDDCm6hLrC3u53FcGrcNQzwR8Xo82jEoZsgHisMIjWfqwhbkV-8OV2MdK2q9Ydmy01KHkAb6l15mGBixUjK1i08w1HyuSzitrHT2g9VKuLdtL5TqWYf0JeC9BS-_hEdVJXleo_ZzoG7kpf2j9_nCzw8CWya-Xl-WSzm8jFRFjaXyrcj7qV9qkVkQlWpxIc4sDaoPsPFvhPGVPI2_-WUQtVUYZ51PDAkofBeG4wwXUyLUcnjCaWIA7YY0FCH_Vkn8VUNje_2mk_UFUT9-YuvI0Pbpm8PZ1ud4YaDXyPXXWgdilYwYQZ_gNItkmq3yzzvP87i7eJNluAU8glnGaruJ0E6_jPMu36SbtF_A81lyvdkmyy5Ik22aengzpUBtH_HBZ7XHD-389KjYS)

### Diagrama de Base de Datos  
[![Ver Diagrama de BD](https://img.shields.io/badge/Diagrama%20BD-0A66C2?style=for-the-badge&logo=mermaid&logoColor=white)](https://mermaid.live/edit#pako:eNp9k0Gv0zAMx79K5RPwtqntuvW1By4PcUNCenBBvZjEr4to48lJJWDah-Iz8MXIthayoi2n-Gc7_sdODqBYE9SgOnTuncFWsG9sEtaZJJ_dgGI4OVzgaS2NHmnELPdfhSKgWIT4Clgv6Oj3L4yocPfPehBqjQtR8up1RI01yqA8kzNsJ8-xsbHOp86Q9TTTOdJ7OrURUiocHDFPHb2w5TvXeXDcBVUe5WOHt0SdXM_UDmI06pm0ky8uafY8Q5qcErOfaVPsPH8gGyYQdw6daS3KeOEbet6j8oPgTMlII_ZCahfbfRgdf2J_VbElS2FSY_Z_FaeH00DWQLJcvg27N2E3DapOpmFf4ic-j7_uYZ1Mfb-fNd20Ti4yL9HXZ93OQcfhwekwblhAG6Kh9jLQAnqSHk8mnFvYgN9RTw3UYatRvjXQ2GPI2aP9wtxPacJDu5uMYa_R0_jV_kaQ1SRPPFgPdZY9no-A-gDfoV7n1Wq7LtaPZZUWRZWl2wX8CFFVsdrk27wKjnJd5tvyuICf56rZqsizrFhvinSTp1mZlsc_Q_gmKg)

---
## 🛠️ Script de Creación

```sql
-- Crear Base de Datos
CREATE DATABASE EmpresaSeguridad;
USE EmpresaSeguridad;
```


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
