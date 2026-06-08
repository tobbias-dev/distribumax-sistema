package dao;

import java.util.ArrayList;
import java.util.List;
import modelo.Producto;

/**
 * Implementacion en memoria del DAO de Producto. Permite ejecutar y
 * probar el prototipo sin un servidor MySQL en linea. La version JDBC
 * (no incluida en la demo ejecutable) implementaria esta misma interfaz
 * GenericDAO<Producto> usando la clase Conexion y sentencias SQL
 * preparadas contra la tabla productos.
 */
public class ProductoDAOMemoria implements GenericDAO<Producto> {

    private final List<Producto> datos = new ArrayList<>();

    @Override
    public void insertar(Producto producto) {
        datos.add(producto);
    }

    @Override
    public Producto buscarPorId(int id) {
        for (Producto p : datos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Producto> listar() {
        return new ArrayList<>(datos);
    }
}
