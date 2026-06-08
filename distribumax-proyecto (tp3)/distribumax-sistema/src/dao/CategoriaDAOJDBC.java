package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;

/**
 * DAO JDBC para la entidad Categoria. Lee y escribe contra la tabla
 * categorias de la base distribumax_db en MySQL.
 */
public class CategoriaDAOJDBC implements GenericDAO<Categoria> {

    @Override
    public void insertar(Categoria c) {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar categoria: " + e.getMessage(), e);
        }
    }

    @Override
    public Categoria buscarPorId(int id) {
        String sql = "SELECT id_categoria, nombre, descripcion FROM categorias WHERE id_categoria = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(rs.getInt("id_categoria"),
                        rs.getString("nombre"), rs.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoria: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion FROM categorias ORDER BY id_categoria";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("id_categoria"),
                    rs.getString("nombre"), rs.getString("descripcion")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar categorias: " + e.getMessage(), e);
        }
        return lista;
    }
}
