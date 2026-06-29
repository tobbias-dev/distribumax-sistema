package dao;

import java.util.ArrayList;
import java.util.List;

import modelo.Pedido;

public class PedidoDAOMemoria implements PedidoDAO {

    private final List<Pedido> pedidos = new ArrayList<>();
    private int secuencia = 0;

    @Override
    public int guardarConDetalle(Pedido pedido) {
        // El descuento de stock ya lo hizo PedidoService en memoria; acá solo
        // asigno el id y guardo el pedido.
        secuencia++;
        pedido.setId(secuencia);
        pedidos.add(pedido);
        return secuencia;
    }

    @Override
    public void insertar(Pedido pedido) {
        guardarConDetalle(pedido);
    }

    @Override
    public Pedido buscarPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Pedido> listar() {
        return new ArrayList<>(pedidos);
    }
}
