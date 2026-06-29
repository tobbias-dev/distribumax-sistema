package servicio;

import dao.PedidoDAO;
import dao.ProductoDAO;
import excepciones.StockInsuficienteException;
import modelo.DetallePedido;
import modelo.Pedido;
import modelo.Producto;

/**
 * Lógica del registro de pedidos (caso de uso CU01). Valida el stock en el
 * mismo momento de agregar cada ítem y, al confirmar, delega la persistencia
 * transaccional en el DAO.
 */
public class PedidoService {

    private final ProductoDAO productoDAO;
    private final PedidoDAO pedidoDAO;

    public PedidoService(ProductoDAO productoDAO, PedidoDAO pedidoDAO) {
        this.productoDAO = productoDAO;
        this.pedidoDAO = pedidoDAO;
    }

    /** Crea un pedido nuevo en estado PENDIENTE para un cliente y vendedor. */
    public Pedido iniciarPedido(int idCliente, int idVendedor) {
        return new Pedido(idCliente, idVendedor);
    }

    /**
     * Agrega un ítem al pedido validando la disponibilidad. Descuenta el stock
     * en el objeto en memoria; la escritura definitiva en la base se hace
     * recién al confirmar, dentro de la transacción.
     */
    public void agregarItem(Pedido pedido, int idProducto, int cantidad)
            throws StockInsuficienteException {

        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            throw new IllegalArgumentException("No existe el producto con id " + idProducto + ".");
        }
        producto.getStock().descontar(cantidad); // valida y descuenta en memoria
        pedido.agregarDetalle(new DetallePedido(producto, cantidad));
    }

    /**
     * Confirma el pedido: lo persiste con su detalle y el descuento de stock
     * de forma atómica. Devuelve el id asignado por la base.
     */
    public int confirmarPedido(Pedido pedido) {
        if (pedido.estaVacio()) {
            throw new IllegalArgumentException("No se puede confirmar un pedido sin productos.");
        }
        return pedidoDAO.guardarConDetalle(pedido);
    }
}
