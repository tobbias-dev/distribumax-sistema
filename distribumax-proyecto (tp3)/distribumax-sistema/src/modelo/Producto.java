package modelo;

/**
 * Producto del catalogo. Contiene su nivel de Stock por COMPOSICION
 * (el Stock no existe sin el Producto que lo contiene), tal como se
 * modelo en el diagrama de clases del TP2.
 */
public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private Categoria categoria;
    private Stock stock; // composicion 1-1

    public Producto(int id, String nombre, String descripcion, double precioUnitario,
                    Categoria categoria, int cantidadInicial, int stockMinimo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.categoria = categoria;
        // El producto crea su propio Stock -> composicion
        this.stock = new Stock(id, cantidadInicial, stockMinimo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Stock getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return nombre + " ($" + String.format("%.2f", precioUnitario) + ")";
    }
}
