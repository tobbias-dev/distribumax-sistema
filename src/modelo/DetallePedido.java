package modelo;

/**
 * Línea de un pedido: un producto, su cantidad y el precio unitario al
 * momento de la venta. Guardo el precio acá (y no solo en Producto) para
 * preservar el historial frente a cambios de tarifa, como anoté en el TP2.
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
