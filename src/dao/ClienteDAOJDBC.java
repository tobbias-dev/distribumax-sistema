package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexion.Conexion;
import excepciones.AccesoDatosException;
import modelo.Cliente;

public class ClienteDAOJDBC implements ClienteDAO {

    @Override
    public Cliente buscarPorId(int id) {
        String sql = "SELECT id_cliente, nombre, direccion, telefono, email "
                + "FROM clientes WHERE id_cliente = ?";
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el cliente con id " + id + ".", e);
        }
        return null;
    }

    @Override
    public List<Cliente> listar() {
        String sql = "SELECT id_cliente, nombre, direccion, telefono, email FROM clientes";
        List<Cliente> clientes = new ArrayList<>();
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los clientes.", e);
        }
        return clientes;
    }

    @Override
    public void insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, direccion, telefono, email) "
                + "VALUES (?, ?, ?, ?)";
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al insertar el cliente.", e);
        }
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
