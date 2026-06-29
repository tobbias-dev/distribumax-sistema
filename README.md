# DistribuMax — Sistema de Gestión de Inventario y Logística (TP4)

Prototipo del proyecto integrador de Seminario de Práctica de Informática.
Desarrollado en Java con persistencia en MySQL, aplicando los patrones DAO y
Singleton sobre una arquitectura MVC.

Alumno: Tobias Uriel Barmaimon Molina

## Estructura

```
ProyectoTP4/
├── src/
│   ├── modelo/        Clases del dominio (Persona abstracta, Usuario, Cliente,
│   │                  Producto, Stock, Pedido, DetallePedido, enums, Hash)
│   ├── dao/           Interfaces GenericDAO + específicas, implementaciones
│   │                  JDBC y en memoria, y DAOFactory
│   ├── conexion/      Conexion (Singleton JDBC)
│   ├── servicio/      InventarioService, PedidoService, AuthService, ReporteArchivo
│   ├── excepciones/   StockInsuficienteException, AccesoDatosException
│   └── vista/         Main (menú de consola)
├── distribumax_db.sql  Script de base: creación, datos de prueba y consultas
└── README.md
```

## Requisitos

- JDK 17 o superior
- MySQL 8.0 (o MariaDB compatible)
- Driver JDBC de MySQL (mysql-connector-j) en el classpath

## Puesta en marcha

1. Crear la base y cargar los datos:

   ```bash
   mysql -u root -p < distribumax_db.sql
   ```

2. Verificar los datos de conexión en `src/conexion/Conexion.java`
   (URL, usuario y contraseña).

3. Compilar:

   ```bash
   javac -d bin $(find src -name "*.java")
   ```

4. Ejecutar contra MySQL:

   ```bash
   java -cp bin:mysql-connector-j.jar vista.Main
   ```

   O ejecutar sin servidor, con datos en memoria (mismos datos de prueba):

   ```bash
   java -cp bin vista.Main memoria
   ```

## Credenciales de prueba

| Usuario    | Contraseña | Rol           |
|------------|------------|---------------|
| admin      | admin123   | ADMIN         |
| vendedor1  | vend123    | VENDEDOR      |
| chofer1    | chof123    | TRANSPORTISTA |

## Funcionalidades del menú

1. Listar inventario
2. Ver alertas de stock mínimo (RF03)
3. Registrar pedido con validación y descuento de stock (CU01)
4. Ingresar mercadería
5. Listar productos por precio (ordenamiento por inserción sobre arreglo)
6. Buscar producto por nombre (búsqueda lineal)
7. Exportar reporte de inventario a archivo de texto
