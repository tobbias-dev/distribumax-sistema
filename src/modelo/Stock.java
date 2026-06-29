package modelo;

import excepciones.StockInsuficienteException;

/**
 * Nivel de stock de un producto. Es el caso más claro de encapsulamiento del
 * proyecto: la cantidad no se puede tocar desde afuera, solo a través de
 * descontar() o incrementar(), que son los que controlan que no se hagan
 * operaciones inválidas.
 */
public class Stock {

    private int idStock;
    private int idProducto;
    private int cantidad;
    private int stockMinimo;

    public Stock(int idStock, int idProducto, int cantidad, int stockMinimo) {
        this.idStock = idStock;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
    }

    public int getIdStock() {
        return idStock;
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

    /**
     * Descuenta unidades validando que haya disponibilidad. Si se piden más
     * de las que hay, lanza la excepción comprobada en lugar de dejar el
     * stock en negativo.
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

    /** true si el stock cayó en o por debajo del mínimo (sustenta RF03). */
    public boolean enAlerta() {
        return cantidad <= stockMinimo;
    }
}
