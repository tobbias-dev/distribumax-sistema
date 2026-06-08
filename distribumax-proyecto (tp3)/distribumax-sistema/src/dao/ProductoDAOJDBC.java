package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.Producto;

/**
 * DAO JDBC para Producto. Trabaja en conjunto con las tablas productos y
 * stock: al leer un producto recupera tambien su nivel de stock mediante
 * un JOIN, y al actualizar persiste el nuevo valor de cantidad.
 *
 * Demuestra la conexion real a MySQL exigida por la consigna, usando
 * PreparedStatement (sentencias preparadas) para evitar inyeccion SQL.
 */
public class ProductoDAOJDBC implements GenericDAO<Producto> {

    @Override
    public void insertar(Producto p) {
        String sqlProd = "INSERT INTO productos (nombre, descripcion, precio_unitario, id_categoria) "
                       + "VALUES (?, ?, ?, ?)";
        String sqlStock = "INSERT INTO stock (id_producto, cantidad, stock_minimo) VALUES (?, ?, ?)";
        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false); // transaccion: producto + stock juntos
            int idGenerado;
            try (PreparedStatement ps = con.prepareStatement(sqlProd,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getDescripcion());
                ps.setDouble(3, p.getPrecioUnitario());
                ps.setInt(4, p.getCategoria().getId());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    idGenerado = rs.getInt(1);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlStock)) {
                ps.setInt(1, idGenerado);
                ps.setInt(2, p.getStock().getCantidad());
                ps.setInt(3, p.getStock().getStockMinimo());
                ps.executeUpdate();
            }
            con.commit();
            p.setId(idGenerado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public Producto buscarPorId(int id) {
        String sql = consultaBase() + " WHERE p.id_producto = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = consultaBase() + " ORDER BY p.id_producto";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Persiste la cantidad de stock actual del producto (UPDATE).
     */
    @Override
    public void actualizar(Producto p) {
        String sql = "UPDATE stock SET cantidad = ? WHERE id_producto = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getStock().getCantidad());
            ps.setInt(2, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar stock: " + e.getMessage(), e);
        }
    }

    private String consultaBase() {
        return "SELECT p.id_producto, p.nombre, p.descripcion, p.precio_unitario, "
             + "c.id_categoria, c.nombre AS cat_nombre, c.descripcion AS cat_desc, "
             + "s.cantidad, s.stock_minimo "
             + "FROM productos p "
             + "JOIN categorias c ON c.id_categoria = p.id_categoria "
             + "JOIN stock s ON s.id_producto = p.id_producto";
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Categoria cat = new Categoria(rs.getInt("id_categoria"),
            rs.getString("cat_nombre"), rs.getString("cat_desc"));
        return new Producto(
            rs.getInt("id_producto"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getDouble("precio_unitario"),
            cat,
            rs.getInt("cantidad"),
            rs.getInt("stock_minimo"));
    }
}
