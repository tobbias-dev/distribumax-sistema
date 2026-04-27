package distribumax.model;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private int idCategoria;

    public Producto() {}

    public Producto(int id, String nombre, String descripcion,
                    double precioUnitario, int idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.idCategoria = idCategoria;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String d) { this.descripcion = d; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double p) { this.precioUnitario = p; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int c) { this.idCategoria = c; }

    @Override
    public String toString() { return nombre + " ($" + precioUnitario + ")"; }
}
