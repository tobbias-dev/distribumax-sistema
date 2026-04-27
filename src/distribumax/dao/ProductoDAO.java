package distribumax.dao;

import distribumax.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try (Connection con = Conexion.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio_unitario"),
                    rs.getInt("id_categoria")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error listar: " + e.getMessage());
        }
        return lista;
    }

    public boolean agregar(Producto p) {
        String sql = "INSERT INTO producto (nombre, descripcion, precio_unitario, id_categoria) VALUES (?,?,?,?)";
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecioUnitario());
            ps.setInt(4, p.getIdCategoria());
            if (ps.executeUpdate() > 0) {
                ResultSet k = ps.getGeneratedKeys();
                if (k.next()) crearStock(con, k.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error agregar: " + e.getMessage());
        }
        return false;
    }

    private void crearStock(Connection con, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO stock (id_producto, cantidad, stock_minimo) VALUES (?,0,10)")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int obtenerStock(int idProducto) {
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(
                "SELECT cantidad FROM stock WHERE id_producto=?")) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("cantidad");
        } catch (SQLException e) {
            System.err.println("Error stock: " + e.getMessage());
        }
        return 0;
    }

    public boolean actualizarStock(int idProducto, int cantidad) {
        try (Connection con = Conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(
                "UPDATE stock SET cantidad=? WHERE id_producto=?")) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizar: " + e.getMessage());
        }
        return false;
    }
}
