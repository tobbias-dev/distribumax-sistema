package controlador;

import dao.GenericDAO;
import modelo.Cliente;
import modelo.DetallePedido;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Producto;
import modelo.StockInsuficienteException;
import modelo.Usuario;

/**
 * Controlador del modulo de pedidos. Implementa el caso de uso central
 * del negocio, CU01 - Registrar Pedido: valida disponibilidad de stock,
 * descuenta inventario y persiste el pedido en una sola operacion.
 *
 * Equivale a la clase de control GestorPedido del modelo de analisis
 * del TP2.
 */
public class PedidoController {

    private final GenericDAO<Pedido> pedidoDAO;
    private final GenericDAO<Producto> productoDAO;
    private int siguienteId = 1;

    public PedidoController(GenericDAO<Pedido> pedidoDAO, GenericDAO<Producto> productoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.productoDAO = productoDAO;
    }

    public Pedido nuevoPedido(Cliente cliente, Usuario vendedor) {
        return new Pedido(siguienteId, cliente, vendedor);
    }

    /**
     * Agrega un item al pedido validando stock antes de descontar.
     * Propaga StockInsuficienteException para que la vista la informe.
     */
    public void agregarItem(Pedido pedido, Producto producto, int cantidad)
            throws StockInsuficienteException {
        // 1. Valida y descuenta stock (puede lanzar excepcion)
        producto.getStock().descontar(cantidad);
        // 2. Persiste el nuevo nivel de stock en la base
        productoDAO.actualizar(producto);
        // 3. Si el descuento fue exitoso, suma la linea al pedido
        pedido.agregarDetalle(new DetallePedido(producto, cantidad));
    }

    /**
     * Confirma el pedido: lo deja en estado PENDIENTE y lo persiste.
     */
    public void confirmar(Pedido pedido) {
        if (pedido.getDetalles().isEmpty()) {
            throw new IllegalStateException("No se puede confirmar un pedido sin productos.");
        }
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedidoDAO.insertar(pedido);
        siguienteId++;
    }
}
