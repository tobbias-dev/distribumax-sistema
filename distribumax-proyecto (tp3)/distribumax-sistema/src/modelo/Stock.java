package modelo;

/**
 * Nivel de stock de un producto. Controla la cantidad disponible y el
 * umbral minimo. El metodo descontar() aplica el manejo de excepciones
 * exigido por la consigna lanzando StockInsuficienteException.
 */
public class Stock {

    private int idProducto;
    private int cantidad;
    private int stockMinimo;

    public Stock(int idProducto, int cantidad, int stockMinimo) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    /**
     * Descuenta unidades del stock disponible.
     * @throws StockInsuficienteException si se pide mas de lo disponible.
     */
    public void descontar(int unidades) throws StockInsuficienteException {
        if (unidades <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser positiva.");
        }
        if (unidades > cantidad) {
            throw new StockInsuficienteException(
                "Stock insuficiente: disponible " + cantidad + ", solicitado " + unidades + ".");
        }
        cantidad -= unidades;
    }

    public void incrementar(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("La cantidad a incrementar debe ser positiva.");
        }
        cantidad += unidades;
    }

    /**
     * Indica si el stock alcanzo o cayo por debajo del umbral minimo (RF03).
     */
    public boolean enAlerta() {
        return cantidad <= stockMinimo;
    }
}
