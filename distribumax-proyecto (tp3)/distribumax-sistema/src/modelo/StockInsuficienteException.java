package modelo;

/**
 * Excepcion de dominio. Se lanza cuando se intenta descontar mas
 * unidades de las disponibles. Es una excepcion COMPROBADA (extiende
 * Exception) para forzar su tratamiento explicito mediante try-catch,
 * cumpliendo el requisito de manejo de excepciones de la consigna.
 */
public class StockInsuficienteException extends Exception {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
