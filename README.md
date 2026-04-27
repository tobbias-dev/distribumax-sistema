# DistribuMax S.A. — Sistema de Gestión de Inventario y Logística

Prototipo operacional desarrollado en Java con persistencia en MySQL.  
Materia: Seminario de Informática  
Trabajo Práctico: AP1 — Primer Seminario  

---

## Tecnologías utilizadas

- Lenguaje: Java (JDK 17)
- Base de datos: MySQL 8.0
- Conectividad: JDBC (Java Database Connectivity)
- Patrón: MVC (Modelo-Vista-Controlador)

---

## Estructura del proyecto
src/distribumax/
├── Main.java              -> Punto de entrada
├── model/
│   ├── Producto.java      -> Clase de dominio Producto
│   └── Pedido.java        -> Clase de dominio Pedido
├── dao/
│   ├── Conexion.java      -> Conexión JDBC a MySQL
│   └── ProductoDAO.java   -> Acceso a datos de productos
├── controller/
│   └── ProductoController.java -> Lógica de negocio
└── view/
└── MenuPrincipal.java -> Interfaz de usuario por consola
sql/
└── distribumax_schema.sql -> Script de creación de base de datos

---

## Cómo ejecutar el proyecto

### 1. Base de datos
1. Instalar MySQL 8.0 o XAMPP
2. Ejecutar el archivo `sql/distribumax_schema.sql` en MySQL
3. Verificar que la base de datos `distribumax` fue creada

### 2. Driver JDBC
Descargar `mysql-connector-j` desde:  
https://dev.mysql.com/downloads/connector/j/  
Agregar el `.jar` al classpath del proyecto.

### 3. Configuración
En `src/distribumax/dao/Conexion.java` verificar:
- URL: `jdbc:mysql://localhost:3306/distribumax`
- Usuario: `root`
- Contraseña: (vacía por defecto en XAMPP)

### 4. Compilar y ejecutar
```bash
javac -cp mysql-connector-j.jar src/distribumax/**/*.java
java -cp .:mysql-connector-j.jar distribumax.Main
```

---

## Módulos implementados

- Gestión de catálogo de productos
- Control de stock en tiempo real
- Alerta automática de stock mínimo
- Estructura MVC completa con JDBC
- Gestión de pedidos (en desarrollo)
- Asignación de rutas (en desarrollo)
- Reportes (en desarrollo)
