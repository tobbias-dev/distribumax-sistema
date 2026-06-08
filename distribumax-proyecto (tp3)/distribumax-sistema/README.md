# DistribuMax - Sistema de Gestion de Inventario y Logistica (TP3)

Prototipo operacional desarrollado para el TP3 de Seminario de Practica de
Informatica (Universidad Siglo 21). Continua el analisis y diseno de los
TP1 y TP2 del proyecto DistribuMax S.A.

## Tecnologias
- Java (JDK 17+)
- MySQL 8.0 (persistencia real por JDBC)
- Patron arquitectonico MVC, acceso a datos por DAO

## Estructura
```
src/
 |- modelo/        Clases del dominio (Persona, Usuario, Cliente, Producto,
 |                 Stock, Pedido, DetallePedido, enums y excepcion propia)
 |- dao/           Conexion MySQL, interfaz GenericDAO y dos juegos de DAO:
 |                   *DAOJDBC    -> persistencia real en MySQL
 |                   *DAOMemoria -> persistencia en memoria (sin servidor)
 |- controlador/   InventarioController, PedidoController (logica de negocio)
 |- vista/         MenuConsola, Main (memoria) y MainMySQL (conexion real)
sql/
 |- distribumax_db.sql   Creacion de base, tablas, datos y consultas
```

## Opcion A - Ejecutar con MySQL real (cumple la consigna)

1. Crear la base ejecutando el script en MySQL Workbench o consola:
   mysql -u root -p < sql/distribumax_db.sql
2. Descargar el driver JDBC de MySQL (mysql-connector-j) desde
   https://dev.mysql.com/downloads/connector/j/  (elegir "Platform Independent",
   descomprimir y quedarse con el archivo .jar).
3. Si hace falta, ajustar usuario/contrasena en src/dao/Conexion.java.
4. Compilar y ejecutar incluyendo el driver en el classpath:

   En Windows (PowerShell):
     javac -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
     java -cp "bin;mysql-connector-j-8.4.0.jar" vista.MainMySQL

   En Linux/Mac:
     javac -d bin $(find src -name "*.java")
     java -cp "bin:mysql-connector-j-8.4.0.jar" vista.MainMySQL

## Opcion B - Ejecutar sin MySQL (datos en memoria, para probar rapido)
   javac -d bin $(find src -name "*.java")
   java -cp bin vista.Main

## Credenciales de prueba
- admin / admin123
- vendedor1 / vend123
- chofer1 / chof123

## Modulo implementado
Modulo de inventario y registro de pedidos (CU01 - Registrar Pedido), con
control de stock en tiempo real, alertas de minimo (RF03), manejo de
excepciones, ordenamiento por insercion y busqueda lineal.
