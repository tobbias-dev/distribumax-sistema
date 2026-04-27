CREATE DATABASE IF NOT EXISTS distribumax;
USE distribumax;

CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    precio_unitario DECIMAL(10,2) NOT NULL,
    id_categoria INT,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);

CREATE TABLE stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT UNIQUE NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 5,
    FOREIGN KEY (id_producto) REFERENCES producto(id)
);

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    direccion VARCHAR(300),
    telefono VARCHAR(50),
    email VARCHAR(150)
);

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    estado VARCHAR(50) DEFAULT 'Pendiente',
    total DECIMAL(10,2) DEFAULT 0,
    id_cliente INT,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);

CREATE TABLE detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_producto) REFERENCES producto(id)
);

CREATE TABLE vehiculo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patente VARCHAR(20) NOT NULL,
    capacidad DECIMAL(8,2),
    estado VARCHAR(50) DEFAULT 'Disponible'
);

CREATE TABLE empleado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    usuario VARCHAR(100) UNIQUE NOT NULL,
    contrasenia VARCHAR(255) NOT NULL
);

-- Datos de ejemplo
INSERT INTO categoria (nombre) VALUES ('Almacen'), ('Lacteos'), ('Bebidas');

INSERT INTO producto (nombre, precio_unitario, id_categoria)
VALUES ('Arroz 1kg', 850.00, 1),
       ('Leche 1L', 620.00, 2),
       ('Agua 2L', 400.00, 3);

INSERT INTO stock (id_producto, cantidad, stock_minimo)
VALUES (1, 100, 20), (2, 50, 15), (3, 80, 10);
