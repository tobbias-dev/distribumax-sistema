package modelo;

/**
 * Linea de un pedido. Guarda el precio unitario al momento de la venta
 * para preservar el historial frente a cambios de tarifa (decision de
 * diseno documentada en el TP2).
 */
public class DetallePedido {

    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public DetallePedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecioUnitario();
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }
}
