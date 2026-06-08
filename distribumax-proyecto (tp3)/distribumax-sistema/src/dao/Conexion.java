package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de conexion con la base de datos MySQL distribumax_db.
 * Corresponde a la capa de persistencia descrita en el TP2 (JDBC sobre
 * MySQL 8.0). En el prototipo ejecutable, la persistencia efectiva se
 * resuelve con DAO en memoria (ver ClienteDAOMemoria, etc.); esta clase
 * documenta el punto de conexion real que se utiliza al desplegar el
 * sistema sobre el servidor de DistribuMax.
 *
 * Para activar la persistencia real basta con agregar el driver
 * mysql-connector-j al classpath e instanciar los DAO JDBC en lugar de
 * los DAO en memoria desde la clase Main.
 */
public class Conexion {

    private static final String URL =
        "jdbc:mysql://localhost:3306/distribumax_db?useSSL=true&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
