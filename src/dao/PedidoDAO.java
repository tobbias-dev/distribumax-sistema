package dao;

import modelo.Pedido;

public interface PedidoDAO extends GenericDAO<Pedido> {

    /**
     * Persiste el pedido junto con todas sus líneas de detalle. Devuelve el
     * id autogenerado por la base.
     */
    int guardarConDetalle(Pedido pedido);
}
