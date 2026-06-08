package dao;

import java.util.ArrayList;
import java.util.List;
import modelo.Pedido;

/**
 * Implementacion en memoria del DAO de Pedido.
 */
public class PedidoDAOMemoria implements GenericDAO<Pedido> {

    private final List<Pedido> datos = new ArrayList<>();

    @Override
    public void insertar(Pedido pedido) {
        datos.add(pedido);
    }

    @Override
    public Pedido buscarPorId(int id) {
        for (Pedido p : datos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Pedido> listar() {
        return new ArrayList<>(datos);
    }
}
