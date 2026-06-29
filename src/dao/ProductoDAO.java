package dao;

import modelo.Producto;

/**
 * Contrato específico para el acceso a productos. Extiende el genérico y le
 * suma las operaciones propias del inventario que la lógica de negocio
 * necesita (actualizar el stock).
 */
public interface ProductoDAO extends GenericDAO<Producto> {

    /** Persiste la cantidad de stock de un producto luego de un movimiento. */
    void actualizarStock(int idProducto, int nuevaCantidad);
}
