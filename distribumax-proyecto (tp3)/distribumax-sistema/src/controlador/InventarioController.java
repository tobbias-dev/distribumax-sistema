package controlador;

import dao.GenericDAO;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.Producto;
import modelo.StockInsuficienteException;

/**
 * Controlador del modulo de inventario. Coordina el alta de productos,
 * el listado, la consulta de alertas (RF03) y aplica algoritmos de
 * ordenacion y busqueda sobre la coleccion de productos.
 *
 * Equivale a la clase de control GestorStock del modelo de analisis del
 * TP2, dentro del patron MVC.
 */
public class InventarioController {

    private final GenericDAO<Producto> productoDAO;
    private int siguienteId = 1;

    public InventarioController(GenericDAO<Producto> productoDAO) {
        this.productoDAO = productoDAO;
    }

    public Producto altaProducto(String nombre, String descripcion, double precio,
                                 Categoria categoria, int cantidad, int stockMinimo) {
        Producto p = new Producto(siguienteId++, nombre, descripcion, precio,
                                  categoria, cantidad, stockMinimo);
        productoDAO.insertar(p);
        return p;
    }

    public List<Producto> listarProductos() {
        return productoDAO.listar();
    }

    public Producto buscarPorId(int id) {
        return productoDAO.buscarPorId(id);
    }

    /**
     * Productos cuyo stock esta en o por debajo del minimo (RF03).
     */
    public List<Producto> productosEnAlerta() {
        List<Producto> alertas = new ArrayList<>();
        for (Producto p : productoDAO.listar()) {
            if (p.getStock().enAlerta()) {
                alertas.add(p);
            }
        }
        return alertas;
    }

    /**
     * Registra un ingreso de mercaderia incrementando el stock.
     */
    public void ingresarMercaderia(int idProducto, int cantidad) {
        Producto p = productoDAO.buscarPorId(idProducto);
        if (p == null) {
            throw new IllegalArgumentException("No existe el producto con id " + idProducto);
        }
        p.getStock().incrementar(cantidad);
        productoDAO.actualizar(p); // persiste el nuevo stock
    }

    /**
     * Ajusta (descuenta) stock manualmente, propagando la excepcion
     * comprobada si la cantidad supera lo disponible.
     */
    public void descontarStock(int idProducto, int cantidad) throws StockInsuficienteException {
        Producto p = productoDAO.buscarPorId(idProducto);
        if (p == null) {
            throw new IllegalArgumentException("No existe el producto con id " + idProducto);
        }
        p.getStock().descontar(cantidad);
    }

    /**
     * ALGORITMO DE ORDENACION: ordenamiento por insercion (insertion
     * sort) de los productos por precio ascendente. Se implementa de
     * forma manual para evidenciar el manejo de estructuras y bucles
     * anidados, segun lo sugerido por la consigna.
     */
    public List<Producto> ordenarPorPrecio() {
        List<Producto> lista = new ArrayList<>(productoDAO.listar());
        for (int i = 1; i < lista.size(); i++) {
            Producto clave = lista.get(i);
            int j = i - 1;
            while (j >= 0 && lista.get(j).getPrecioUnitario() > clave.getPrecioUnitario()) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, clave);
        }
        return lista;
    }

    /**
     * ALGORITMO DE BUSQUEDA: busqueda lineal por nombre (coincidencia
     * parcial, sin distinguir mayusculas).
     */
    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultado = new ArrayList<>();
        String criterio = texto.toLowerCase();
        for (Producto p : productoDAO.listar()) {
            if (p.getNombre().toLowerCase().contains(criterio)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
