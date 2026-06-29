package modelo;

/**
 * Producto del catálogo. Mantengo Producto y Stock como entidades separadas
 * (composición), tal como quedó en el diagrama de clases del TP2, para poder
 * operar el inventario en tiempo real. El Stock se asocia por id_producto.
 */
public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private int idCategoria;
    private Stock stock;

    public Producto(int id, String nombre, String descripcion, double precioUnitario, int idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.idCategoria = idCategoria;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
