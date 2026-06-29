package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import conexion.Conexion;
import excepciones.AccesoDatosException;
import modelo.DetallePedido;
import modelo.EstadoPedido;
import modelo.Pedido;

/**
 * Implementación JDBC del acceso a pedidos. El método central es
 * guardarConDetalle(), que persiste la cabecera, todas las líneas y el
 * descuento de stock dentro de una misma transacción: o se guarda todo o no
 * se guarda nada. Si algo falla, hago rollback para no dejar la base a medias.
 */
public class PedidoDAOJDBC implements PedidoDAO {

    @Override
    public int guardarConDetalle(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (fecha, estado, total, id_cliente, id_vendedor) "
                + "VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario) "
                + "VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE stock SET cantidad = cantidad - ? WHERE id_producto = ?";

        Connection con = Conexion.getInstancia().getConnection();

        try {
            con.setAutoCommit(false); // arranca la transacción

            int idPedido;
            // 1) Cabecera del pedido, recuperando la clave generada.
            try (PreparedStatement ps = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                ps.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
                ps.setString(2, pedido.getEstado().name());
                ps.setDouble(3, pedido.calcularTotal());
                ps.setInt(4, pedido.getIdCliente());
                ps.setInt(5, pedido.getIdVendedor());
                ps.executeUpdate();

                try (ResultSet claves = ps.getGeneratedKeys()) {
                    if (claves.next()) {
                        idPedido = claves.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el id del pedido.");
                    }
                }
            }

            // 2) Cada línea de detalle + el descuento de stock correspondiente.
            try (PreparedStatement psDet = con.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = con.prepareStatement(sqlStock)) {

                for (DetallePedido d : pedido.getDetalles()) {
                    psDet.setInt(1, idPedido);
                    psDet.setInt(2, d.getProducto().getId());
                    psDet.setInt(3, d.getCantidad());
                    psDet.setDouble(4, d.getPrecioUnitario());
                    psDet.addBatch();

                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getProducto().getId());
                    psStock.addBatch();
                }
                psDet.executeBatch();
                psStock.executeBatch();
            }

            con.commit(); // todo salió bien
            pedido.setId(idPedido);
            return idPedido;

        } catch (SQLException e) {
            // Algo falló: deshago todo lo de esta transacción.
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.err.println("Aviso: falló el rollback: " + ex.getMessage());
            }
            throw new AccesoDatosException("Error al guardar el pedido; se revirtió la operación.", e);
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Aviso: no se pudo restaurar autocommit: " + ex.getMessage());
            }
        }
    }

    @Override
    public void insertar(Pedido pedido) {
        guardarConDetalle(pedido);
    }

    @Override
    public Pedido buscarPorId(int id) {
        String sql = "SELECT id_pedido, fecha, estado, total, id_cliente, id_vendedor "
                + "FROM pedidos WHERE id_pedido = ?";
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pedido p = new Pedido(rs.getInt("id_cliente"), rs.getInt("id_vendedor"));
                    p.setId(rs.getInt("id_pedido"));
                    p.setEstado(EstadoPedido.valueOf(rs.getString("estado")));
                    return p;
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el pedido con id " + id + ".", e);
        }
        return null;
    }

    @Override
    public List<Pedido> listar() {
        String sql = "SELECT id_pedido, fecha, estado, total, id_cliente, id_vendedor FROM pedidos";
        List<Pedido> pedidos = new ArrayList<>();
        Connection con = Conexion.getInstancia().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido(rs.getInt("id_cliente"), rs.getInt("id_vendedor"));
                p.setId(rs.getInt("id_pedido"));
                p.setEstado(EstadoPedido.valueOf(rs.getString("estado")));
                pedidos.add(p);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los pedidos.", e);
        }
        return pedidos;
    }
}
