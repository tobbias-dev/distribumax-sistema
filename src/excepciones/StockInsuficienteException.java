package excepciones;

/**
 * Excepción comprobada (extiende Exception) que se lanza cuando se intenta
 * descontar más unidades de las que hay disponibles. Al ser comprobada,
 * obliga a tratarla, que es justo lo que quiero: que el error de stock no
 * pueda pasarse por alto.
 */
public class StockInsuficienteException extends Exception {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
