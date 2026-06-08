package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

/**
 * DAO JDBC para Cliente. Lee y escribe contra la tabla clientes.
 */
public class ClienteDAOJDBC implements GenericDAO<Cliente> {

    @Override
    public void insertar(Cliente c) {
        String sql = "INSERT INTO clientes (nombre, direccion, telefono, email) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDireccion());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente buscarPorId(int id) {
        String sql = "SELECT id_cliente, nombre, direccion, telefono, email "
                   + "FROM clientes WHERE id_cliente = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_cliente, nombre, direccion, telefono, email "
                   + "FROM clientes ORDER BY id_cliente";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id_cliente"),
            rs.getString("nombre"),
            rs.getString("direccion"),
            rs.getString("telefono"),
            rs.getString("email"));
    }
}
