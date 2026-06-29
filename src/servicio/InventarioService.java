package servicio;

import java.util.ArrayList;
import java.util.List;

import dao.ProductoDAO;
import modelo.Producto;

/**
 * Lógica de negocio del módulo de inventario. Trabaja contra la interfaz
 * ProductoDAO, sin saber si por detrás hay MySQL o memoria (polimorfismo).
 *
 * Acá uso de forma complementaria las dos estructuras que pide la consigna:
 * - ArrayList para los listados dinámicos que vienen de la base.
 * - un arreglo (Producto[]) para el algoritmo de ordenamiento por precio,
 *   donde el tamaño es fijo y conocido en ese momento.
 */
public class InventarioService {

    private final ProductoDAO productoDAO;

    public InventarioService(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    /** Lista completa del inventario (ArrayList recuperado por el DAO). */
    public List<Producto> listarInventario() {
        return productoDAO.listar();
    }

    /** Productos en o por debajo del stock mínimo (RF03). */
    public List<Producto> listarAlertas() {
        List<Producto> alertas = new ArrayList<>();
        for (Producto p : productoDAO.listar()) {
            if (p.getStock().enAlerta()) {
                alertas.add(p);
            }
        }
        return alertas;
    }

    /**
     * Suma unidades al stock de un producto y persiste el cambio en la base.
     * Primero actualizo el objeto en memoria y después escribo el nuevo valor.
     */
    public void ingresarMercaderia(int idProducto, int unidades) {
        Producto p = productoDAO.buscarPorId(idProducto);
        if (p == null) {
            throw new IllegalArgumentException("No existe el producto con id " + idProducto + ".");
        }
        p.getStock().incrementar(unidades);
        productoDAO.actualizarStock(idProducto, p.getStock().getCantidad());
    }

    /**
     * Ordena el catálogo por precio usando un arreglo y un ordenamiento por
     * inserción hecho a mano (insertion sort). Paso la lista del DAO a un
     * arreglo justamente para mostrar el manejo de arreglos además del de la
     * lista.
     */
    public Producto[] ordenarPorPrecio() {
        List<Producto> lista = productoDAO.listar();
        Producto[] arreglo = lista.toArray(new Producto[0]); // List -> arreglo

        for (int i = 1; i < arreglo.length; i++) {
            Producto clave = arreglo[i];
            int j = i - 1;
            while (j >= 0 && arreglo[j].getPrecioUnitario() > clave.getPrecioUnitario()) {
                arreglo[j + 1] = arreglo[j];
                j--;
            }
            arreglo[j + 1] = clave;
        }
        return arreglo;
    }

    /** Búsqueda lineal por nombre (no distingue mayúsculas). */
    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productoDAO.listar()) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
}
