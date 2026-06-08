package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import modelo.DetallePedido;
import modelo.Pedido;

/**
 * DAO JDBC para Pedido. Inserta la cabecera del pedido y todas sus
 * lineas de detalle dentro de una misma TRANSACCION, de modo que si algo
 * falla no quede un pedido a medias. Es la persistencia real del caso de
 * uso CU01.
 */
public class PedidoDAOJDBC implements GenericDAO<Pedido> {

    @Override
    public void insertar(Pedido pedido) {
        String sqlPed = "INSERT INTO pedidos (fecha, estado, total, id_cliente, id_vendedor) "
                      + "VALUES (?, ?, ?, ?, ?)";
        String sqlDet = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario) "
                      + "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false);
            int idPedido;
            try (PreparedStatement ps = con.prepareStatement(sqlPed,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
                ps.setString(2, pedido.getEstado().name());
                ps.setDouble(3, pedido.calcularTotal());
                ps.setInt(4, pedido.getCliente().getId());
                ps.setInt(5, pedido.getVendedor().getId());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    idPedido = rs.getInt(1);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlDet)) {
                for (DetallePedido d : pedido.getDetalles()) {
                    ps.setInt(1, idPedido);
                    ps.setInt(2, d.getProducto().getId());
                    ps.setInt(3, d.getCantidad());
                    ps.setDouble(4, d.getPrecioUnitario());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public Pedido buscarPorId(int id) {
        // No utilizado por el modulo del prototipo; se deja sin implementar
        // la reconstruccion completa para no exceder el alcance del TP3.
        return null;
    }

    @Override
    public List<Pedido> listar() {
        return new ArrayList<>();
    }
}
