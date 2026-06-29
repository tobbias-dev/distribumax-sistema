package dao;

import java.util.ArrayList;
import java.util.List;

import modelo.Producto;
import modelo.Stock;

/**
 * Implementación en memoria del acceso a productos. Carga los mismos datos de
 * prueba que el script SQL, así el comportamiento es idéntico al de la versión
 * MySQL. Sirve para correr el prototipo sin tener el servidor encendido.
 *
 * Es la otra cara del polimorfismo del patrón DAO: implementa la misma
 * interfaz ProductoDAO, de modo que el resto del sistema no nota la diferencia.
 */
public class ProductoDAOMemoria implements ProductoDAO {

    private final List<Producto> productos = new ArrayList<>();

    public ProductoDAOMemoria() {
        cargarDatosDePrueba();
    }

    private void cargarDatosDePrueba() {
        crear(1, "Yerba 1kg", "Yerba mate molida", 1500.00, 1, 85, 20);
        crear(2, "Aceite 1.5L", "Aceite de girasol", 2800.00, 1, 12, 20);
        crear(3, "Fideos 500g", "Fideos guiseros", 650.00, 1, 40, 15);
        crear(4, "Azucar 1kg", "Azucar comun", 950.00, 1, 8, 25);
        crear(5, "Gaseosa 2.25L", "Gaseosa cola", 1800.00, 2, 60, 25);
        crear(6, "Lavandina 1L", "Lavandina concentrada", 900.00, 3, 30, 10);
    }

    private void crear(int id, String nombre, String desc, double precio,
                       int idCat, int cant, int min) {
        Producto p = new Producto(id, nombre, desc, precio, idCat);
        p.setStock(new Stock(id, id, cant, min));
        productos.add(p);
    }

    @Override
    public List<Producto> listar() {
        return new ArrayList<>(productos);
    }

    @Override
    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void insertar(Producto producto) {
        productos.add(producto);
    }

    @Override
    public void actualizarStock(int idProducto, int nuevaCantidad) {
        // En memoria el objeto Stock ya se modificó por referencia; este método
        // existe para respetar la interfaz y mantener el código intercambiable.
    }
}
