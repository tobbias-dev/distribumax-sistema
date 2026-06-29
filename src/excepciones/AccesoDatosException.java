package excepciones;

/**
 * Envuelve los errores de la capa de persistencia. La idea es que el resto
 * del sistema no tenga que conocer SQLException: los DAO atrapan el error
 * técnico de JDBC y lo relanzan como esta excepción, más propia del dominio.
 * Es no comprobada (extiende RuntimeException) para no ensuciar todas las
 * firmas, pero igual la atrapo en la vista para informar al usuario.
 */
public class AccesoDatosException extends RuntimeException {

    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public AccesoDatosException(String mensaje) {
        super(mensaje);
    }
}
