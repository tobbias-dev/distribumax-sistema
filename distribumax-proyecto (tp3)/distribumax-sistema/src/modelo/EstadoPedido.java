package modelo;

/**
 * Estados del ciclo de vida de un pedido (RF05 del TP1/TP2).
 * Replica el ENUM de la columna estado de la tabla pedidos.
 */
public enum EstadoPedido {
    PENDIENTE,
    EN_PREPARACION,
    EN_TRANSITO,
    ENTREGADO,
    CANCELADO
}
