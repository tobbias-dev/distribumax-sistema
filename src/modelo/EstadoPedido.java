package modelo;

/**
 * Estados posibles de un pedido. Son exactamente los mismos que definí en el
 * TP2 y que están en el tipo ENUM de la tabla pedidos, para que coincidan
 * Java y la base sin traducciones intermedias.
 */
public enum EstadoPedido {
    PENDIENTE,
    EN_PREPARACION,
    EN_TRANSITO,
    ENTREGADO,
    CANCELADO
}
