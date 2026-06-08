package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Rol;
import modelo.Usuario;

/**
 * DAO JDBC para Usuario. Implementa la interfaz UsuarioDAO contra la
 * tabla usuarios, incluyendo la busqueda por nombre de usuario para el
 * proceso de autenticacion.
 */
public class UsuarioDAOJDBC implements UsuarioDAO {

    @Override
    public void insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (usuario, password_hash, nombre, rol) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getRol().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        return buscar("WHERE id_usuario = ?", id, null);
    }

    @Override
    public Usuario buscarPorUsuario(String nombreUsuario) {
        return buscar("WHERE usuario = ?", 0, nombreUsuario);
    }

    private Usuario buscar(String filtro, int id, String usuario) {
        String sql = "SELECT id_usuario, usuario, password_hash, nombre, rol FROM usuarios " + filtro;
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (usuario != null) {
                ps.setString(1, usuario);
            } else {
                ps.setInt(1, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, usuario, password_hash, nombre, rol FROM usuarios";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return lista;
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
