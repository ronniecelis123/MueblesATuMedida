-- =====================================================
-- MTM DATABASE
-- Sistema de Configuración de Muebles 3D
-- MariaDB
-- =====================================================

DROP DATABASE IF EXISTS mtm;

CREATE DATABASE mtm;

USE mtm;

-- =====================================================
-- ROLES
-- =====================================================

CREATE TABLE rol (

    id_rol INT PRIMARY KEY AUTO_INCREMENT,

    nombre VARCHAR(50) NOT NULL

);

INSERT INTO rol (id_rol, nombre)
VALUES
(1, 'ADMIN'),
(2, 'USUARIO');

-- =====================================================
-- USUARIOS
-- =====================================================

CREATE TABLE usuario (

    id_usuario INT PRIMARY KEY AUTO_INCREMENT,

    nombre VARCHAR(100) NOT NULL,

    email VARCHAR(120) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    activo BOOLEAN DEFAULT TRUE,

    id_rol INT NOT NULL,

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (id_rol)
        REFERENCES rol(id_rol)

);

-- =====================================================
-- REFRESH TOKENS
-- =====================================================

CREATE TABLE refresh_token (

    id_refresh_token INT PRIMARY KEY AUTO_INCREMENT,

    token TEXT NOT NULL,

    fecha_expiracion DATETIME NOT NULL,

    id_usuario INT NOT NULL,

    CONSTRAINT fk_refresh_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)

);

-- =====================================================
-- MATERIALES
-- =====================================================

CREATE TABLE material (

    id_material INT PRIMARY KEY AUTO_INCREMENT,

    nombre VARCHAR(100) NOT NULL,

    costo_m2 DECIMAL(10,2) NOT NULL,

    activo BOOLEAN DEFAULT TRUE

);

INSERT INTO material
(nombre, costo_m2, activo)
VALUES
('Cedro', 200.00, 1),
('Encino', 300.00, 1),
('Nogal', 230.00, 1);

-- =====================================================
-- ACABADOS
-- =====================================================

CREATE TABLE acabado (

    id_acabado INT PRIMARY KEY AUTO_INCREMENT,

    nombre VARCHAR(100) NOT NULL,

    factor_precio DECIMAL(10,2) NOT NULL,

    activo BOOLEAN DEFAULT TRUE

);

INSERT INTO acabado
(nombre, factor_precio, activo)
VALUES
('Estandar', 1.0, 1);

-- =====================================================
-- TIPO PATA
-- =====================================================

CREATE TABLE tipo_pata (

    id_tipo_pata INT PRIMARY KEY AUTO_INCREMENT,

    nombre VARCHAR(100) NOT NULL,

    factor_precio DECIMAL(10,2) NOT NULL,

    activo BOOLEAN DEFAULT TRUE

);

INSERT INTO tipo_pata
(nombre, factor_precio, activo)
VALUES
('Metal', 1.1, 1),
('Industrial', 1.3, 1);

-- =====================================================
-- PRODUCTOS
-- =====================================================

CREATE TABLE producto (

    id_producto INT PRIMARY KEY AUTO_INCREMENT,

    nombre VARCHAR(150) NOT NULL,

    descripcion TEXT,

    precio_base DECIMAL(10,2) NOT NULL,

    imagen_url TEXT,

    activo BOOLEAN DEFAULT TRUE,

    categoria VARCHAR(100),

    base3d TEXT,

    patas3d TEXT,

    modelo_path VARCHAR(100)

);

INSERT INTO producto
(
    nombre,
    descripcion,
    precio_base,
    imagen_url,
    activo,
    categoria,
    base3d,
    patas3d,
    modelo_path
)
VALUES

(
    'Mueble Zeno',
    'Este es un mueble rectangular',
    1200.00,
    '/renders/zeno.png',
    1,
    'Mesa',
    '/models/zeno/zenoCedro.glb',
    '/models/zeno/zenoPatasMetal.glb',
    'zeno'
),

(
    'Mueble Orion',
    'Una mueble Orion',
    2000.00,
    '/renders/orion.png',
    1,
    'Mesa',
    '/models/orion/orionEncino.glb',
    '/models/orion/orionPatasMetal.glb',
    'orion'
),

(
    'Mueble Bevel',
    'Mueble con la orilla circular',
    1500.00,
    '/renders/bevel.png',
    1,
    'Mesa',
    '/models/bevel/bevelEncino.glb',
    '/models/bevel/bevelPatasIndustrial.glb',
    'bevel'
),

(
    'Silla Old School',
    'Míralo como un banco y usa tu imaginación',
    1100.00,
    '/renders/oldschool.png',
    1,
    'Silla',
    '/models/oldschool/oldschoolCedro.glb',
    '/models/oldschool/oldschoolPatasIndustrial.glb',
    'oldschool'
);

-- =====================================================
-- CONFIGURACIONES
-- =====================================================

CREATE TABLE configuracion (

    id_configuracion INT PRIMARY KEY AUTO_INCREMENT,

    id_usuario INT NOT NULL,

    id_producto INT NOT NULL,

    id_material INT NOT NULL,

    id_acabado INT NOT NULL,

    id_tipo_pata INT NOT NULL,

    largo DECIMAL(10,2),

    ancho DECIMAL(10,2),

    alto DECIMAL(10,2),

    precio_calculado DECIMAL(10,2),

    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_config_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario),

    CONSTRAINT fk_config_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto),

    CONSTRAINT fk_config_material
        FOREIGN KEY (id_material)
        REFERENCES material(id_material),

    CONSTRAINT fk_config_acabado
        FOREIGN KEY (id_acabado)
        REFERENCES acabado(id_acabado),

    CONSTRAINT fk_config_pata
        FOREIGN KEY (id_tipo_pata)
        REFERENCES tipo_pata(id_tipo_pata)

);

-- =====================================================
-- CARRITO
-- =====================================================

CREATE TABLE carrito (

    id_carrito INT PRIMARY KEY AUTO_INCREMENT,

    id_usuario INT NOT NULL,

    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,

    estado VARCHAR(50),

    CONSTRAINT fk_carrito_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)

);

-- =====================================================
-- CARRITO DETALLE
-- =====================================================

CREATE TABLE carrito_detalle (

    id_carrito_detalle INT PRIMARY KEY AUTO_INCREMENT,

    id_carrito INT NOT NULL,

    id_configuracion INT NOT NULL,

    cantidad INT NOT NULL,

    precio_unitario DECIMAL(10,2),

    subtotal DECIMAL(10,2),

    CONSTRAINT fk_det_carrito
        FOREIGN KEY (id_carrito)
        REFERENCES carrito(id_carrito),

    CONSTRAINT fk_det_configuracion
        FOREIGN KEY (id_configuracion)
        REFERENCES configuracion(id_configuracion)

);

-- =====================================================
-- ORDEN
-- =====================================================

CREATE TABLE orden (

    id_orden INT PRIMARY KEY AUTO_INCREMENT,

    id_usuario INT NOT NULL,

    fecha_orden DATETIME DEFAULT CURRENT_TIMESTAMP,

    total DECIMAL(10,2),

    estado VARCHAR(50),

    CONSTRAINT fk_orden_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)

);

-- =====================================================
-- ORDEN DETALLE
-- =====================================================

CREATE TABLE orden_detalle (

    id_orden_detalle INT PRIMARY KEY AUTO_INCREMENT,

    id_orden INT NOT NULL,

    id_configuracion INT NOT NULL,

    cantidad INT,

    precio_unitario DECIMAL(10,2),

    subtotal DECIMAL(10,2),

    CONSTRAINT fk_od_orden
        FOREIGN KEY (id_orden)
        REFERENCES orden(id_orden),

    CONSTRAINT fk_od_config
        FOREIGN KEY (id_configuracion)
        REFERENCES configuracion(id_configuracion)

);

-- =====================================================
-- PAGOS
-- =====================================================

CREATE TABLE pago (

    id_pago INT PRIMARY KEY AUTO_INCREMENT,

    id_orden INT NOT NULL,

    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,

    monto DECIMAL(10,2),

    metodo_pago VARCHAR(100),

    estado VARCHAR(50),

    referencia VARCHAR(255),

    CONSTRAINT fk_pago_orden
        FOREIGN KEY (id_orden)
        REFERENCES orden(id_orden)

);

-- =====================================================
-- FIN SCRIPT
-- =====================================================