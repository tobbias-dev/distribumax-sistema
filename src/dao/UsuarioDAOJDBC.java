package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexion.Conexion;
import excepciones.AccesoDatosException;
import modelo.Rol;
import modelo.Usuario;

public class UsuarioDAOJDBC implements UsuarioDAO {

    @Override
    public Usuario buscarPorUsuario(String usuario) {
        String sql = "SELECT id_usuario, usuario, password_hash, nombre, rol "
                + "FROM usuarios WHERE usuario = ?";
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el usuario '" + usuario + "'.", e);
        }
        return null;
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT id_usuario, usuario, password_hash, nombre, rol "
                + "FROM usuarios WHERE id_usuario = ?";
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el usuario con id " + id + ".", e);
        }
        return null;
    }

    @Override
    public List<Usuario> listar() {
        String sql = "SELECT id_usuario, usuario, password_hash, nombre, rol FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los usuarios.", e);
        }
        return usuarios;
    }

    @Override
    public void insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (usuario, password_hash, nombre, rol) "
                + "VALUES (?, ?, ?, ?)";
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getPasswordHash());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getRol().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al insertar el usuario.", e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("usuario"),
                rs.getString("password_hash"),
                Rol.valueOf(rs.getString("rol")));
    }
}
