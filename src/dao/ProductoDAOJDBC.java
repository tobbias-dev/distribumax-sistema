package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexion.Conexion;
import excepciones.AccesoDatosException;
import modelo.Producto;
import modelo.Stock;

/**
 * Implementación JDBC del acceso a productos. Cada método usa
 * PreparedStatement (en lugar de armar el SQL concatenando strings) para
 * evitar inyección SQL, y atrapa SQLException relanzándolo como
 * AccesoDatosException para que la capa superior no dependa de JDBC.
 *
 * Toda la conexión sale del Singleton Conexion: este DAO nunca abre una
 * conexión propia.
 */
public class ProductoDAOJDBC implements ProductoDAO {

    @Override
    public List<Producto> listar() {
        // Traigo el producto junto con su stock en una sola consulta.
        String sql = "SELECT p.id_producto, p.nombre, p.descripcion, p.precio_unitario, "
                + "p.id_categoria, s.id_stock, s.cantidad, s.stock_minimo "
                + "FROM productos p JOIN stock s ON s.id_producto = p.id_producto "
                + "ORDER BY p.id_producto";

        List<Producto> productos = new ArrayList<>();
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los productos.", e);
        }
        return productos;
    }

    @Override
    public Producto buscarPorId(int id) {
        String sql = "SELECT p.id_producto, p.nombre, p.descripcion, p.precio_unitario, "
                + "p.id_categoria, s.id_stock, s.cantidad, s.stock_minimo "
                + "FROM productos p JOIN stock s ON s.id_producto = p.id_producto "
                + "WHERE p.id_producto = ?";

        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el producto con id " + id + ".", e);
        }
        return null;
    }

    @Override
    public void insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio_unitario, id_categoria) "
                + "VALUES (?, ?, ?, ?)";
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecioUnitario());
            ps.setInt(4, producto.getIdCategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al insertar el producto.", e);
        }
    }

    @Override
    public void actualizarStock(int idProducto, int nuevaCantidad) {
        String sql = "UPDATE stock SET cantidad = ? WHERE id_producto = ?";
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idProducto);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new AccesoDatosException(
                        "No se encontró stock para el producto " + idProducto + ".");
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar el stock.", e);
        }
    }

    /** Arma un Producto (con su Stock asociado) a partir de una fila del ResultSet. */
    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio_unitario"),
                rs.getInt("id_categoria"));

        Stock s = new Stock(
                rs.getInt("id_stock"),
                rs.getInt("id_producto"),
                rs.getInt("cantidad"),
                rs.getInt("stock_minimo"));

        p.setStock(s);
        return p;
    }
}
