package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import excepciones.AccesoDatosException;

/**
 * Conexión a la base MySQL implementada como Singleton.
 *
 * El sentido del Singleton acá es concreto: que en toda la aplicación exista
 * una única instancia que centralice la conexión a la base, en lugar de que
 * cada DAO abra la suya por las suyas. Eso evita multiplicar conexiones,
 * mantiene un único punto de configuración (URL, usuario, contraseña) y hace
 * más prolijo el manejo del recurso.
 *
 * El constructor es privado para que nadie pueda crear instancias desde
 * afuera; el único acceso es a través de getInstancia().
 */
public class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3306/distribumax_db";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "tobidev2550";

    // Única instancia de la clase (esencia del Singleton).
    private static Conexion instancia;

    private Connection connection;

    // Constructor privado: nadie instancia Conexion desde afuera.
    private Conexion() {
    }

    /**
     * Devuelve la única instancia, creándola la primera vez que se la pide.
     * Sincronizado para que sea seguro si en algún momento se usa desde
     * varios hilos.
     */
    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    /**
     * Devuelve la conexión activa, abriéndola si todavía no existe o si quedó
     * cerrada. Si falla, envuelve el SQLException en una excepción del dominio
     * para no obligar a toda la app a conocer la API de JDBC.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo establecer la conexión con MySQL.", e);
        }
    }

    /** Cierra la conexión al terminar el programa. */
    public void cerrar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Al cerrar no quiero tirar el programa abajo; solo dejo constancia.
            System.err.println("Aviso: no se pudo cerrar la conexión: " + e.getMessage());
        }
    }
}
