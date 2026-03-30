-- Crear base de datos
drop database mtm;
CREATE DATABASE IF NOT EXISTS mtm;
USE mtm;

-- 1. Tabla ROL

CREATE TABLE rol (
  id_rol        INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(50) NOT NULL
);


-- 2. Tabla USUARIO

CREATE TABLE usuario (
  id_usuario    INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  email         VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  id_rol        INT NOT NULL,
  CONSTRAINT fk_usuario_rol
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);


-- 3. Tabla PRODUCTO

CREATE TABLE producto (
  id_producto   INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  descripcion   VARCHAR(255),
  precio_base   DECIMAL(10,2) NOT NULL,
  imagen_url    VARCHAR(255),
  categoria     VARCHAR(255) NOT NULL,
  activo        BOOLEAN NOT NULL DEFAULT TRUE
);


-- 4. Tabla MATERIAL

CREATE TABLE material (
  id_material   INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  costo_m2      DECIMAL(10,2) NOT NULL,
  activo        BOOLEAN NOT NULL DEFAULT TRUE
);

-- 5. Tabla ACABADO
CREATE TABLE acabado (
  id_acabado    INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  factor_precio DECIMAL(5,2) NOT NULL,
  activo        BOOLEAN NOT NULL DEFAULT TRUE
);


-- 6. Tabla TIPO_PATA

CREATE TABLE tipo_pata (
  id_tipo_pata  INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  factor_precio DECIMAL(5,2) NOT NULL,
  activo        BOOLEAN NOT NULL DEFAULT TRUE
);


-- 7. Tabla CONFIGURACION

CREATE TABLE configuracion (
  id_configuracion  INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario        INT NOT NULL,
  id_producto       INT NOT NULL,
  largo             DECIMAL(6,2) NOT NULL,
  ancho             DECIMAL(6,2) NOT NULL,
  alto              DECIMAL(6,2) NOT NULL,
  id_material       INT NOT NULL,
  id_acabado        INT NOT NULL,
  id_tipo_pata      INT NOT NULL,
  precio_calculado  DECIMAL(10,2) NOT NULL,
  fecha_creacion    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_conf_usuario
    FOREIGN KEY (id_usuario)   REFERENCES usuario(id_usuario),
  CONSTRAINT fk_conf_producto
    FOREIGN KEY (id_producto)  REFERENCES producto(id_producto),
  CONSTRAINT fk_conf_material
    FOREIGN KEY (id_material)  REFERENCES material(id_material),
  CONSTRAINT fk_conf_acabado
    FOREIGN KEY (id_acabado)   REFERENCES acabado(id_acabado),
  CONSTRAINT fk_conf_tipo_pata
    FOREIGN KEY (id_tipo_pata) REFERENCES tipo_pata(id_tipo_pata)
);


-- 8. Tabla CARRITO

CREATE TABLE carrito (
  id_carrito     INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario     INT NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  estado         VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
  CONSTRAINT fk_carrito_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);


-- 9. Tabla CARRITO_DETALLE
CREATE TABLE carrito_detalle (
  id_carrito_detalle INT AUTO_INCREMENT PRIMARY KEY,
  id_carrito         INT NOT NULL,
  id_configuracion   INT NOT NULL,
  cantidad           INT NOT NULL,
  precio_unitario    DECIMAL(10,2) NOT NULL,
  subtotal           DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_cdet_carrito
    FOREIGN KEY (id_carrito)       REFERENCES carrito(id_carrito),
  CONSTRAINT fk_cdet_configuracion
    FOREIGN KEY (id_configuracion) REFERENCES configuracion(id_configuracion)
);


-- 10. Tabla ORDEN

CREATE TABLE orden (
  id_orden       INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario     INT NOT NULL,
  id_carrito     INT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  total          DECIMAL(10,2) NOT NULL,
  estado         VARCHAR(20) NOT NULL,
  codigo_cupon   VARCHAR(50),
  CONSTRAINT fk_orden_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  CONSTRAINT fk_orden_carrito
    FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito)
);


-- 11. Tabla ORDEN_DETALLE

CREATE TABLE orden_detalle (
  id_orden_detalle INT AUTO_INCREMENT PRIMARY KEY,
  id_orden         INT NOT NULL,
  id_configuracion INT NOT NULL,
  cantidad         INT NOT NULL,
  precio_unitario  DECIMAL(10,2) NOT NULL,
  subtotal         DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_odet_orden
    FOREIGN KEY (id_orden)        REFERENCES orden(id_orden),
  CONSTRAINT fk_odet_configuracion
    FOREIGN KEY (id_configuracion) REFERENCES configuracion(id_configuracion)
);


-- 12. Tabla PAGO

CREATE TABLE pago (
  id_pago      INT AUTO_INCREMENT PRIMARY KEY,
  id_orden     INT NOT NULL,
  fecha_pago   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  monto        DECIMAL(10,2) NOT NULL,
  metodo_pago  VARCHAR(50) NOT NULL,
  estado       VARCHAR(50) NOT NULL,
  referencia   VARCHAR(100),
  CONSTRAINT fk_pago_orden
    FOREIGN KEY (id_orden) REFERENCES orden(id_orden)
);
