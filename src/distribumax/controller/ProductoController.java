package distribumax.controller;

import distribumax.dao.ProductoDAO;
import distribumax.model.Producto;
import java.util.List;

public class ProductoController {
    private final ProductoDAO dao = new ProductoDAO();

    public List<Producto> obtenerTodos() { return dao.listarTodos(); }

    public boolean registrarProducto(String nombre, String desc, double precio, int cat) {
        if (nombre == null || nombre.trim().isEmpty() || precio <= 0) return false;
        return dao.agregar(new Producto(0, nombre.trim(), desc, precio, cat));
    }

    public int consultarStock(int id) { return dao.obtenerStock(id); }

    public boolean descontarStock(int id, int cantidad) {
        int actual = consultarStock(id);
        if (actual < cantidad) return false;
        return dao.actualizarStock(id, actual - cantidad);
    }
}
