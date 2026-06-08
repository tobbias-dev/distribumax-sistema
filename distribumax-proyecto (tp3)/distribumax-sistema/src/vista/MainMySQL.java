package vista;

import controlador.InventarioController;
import controlador.PedidoController;
import dao.ClienteDAOJDBC;
import dao.GenericDAO;
import dao.PedidoDAOJDBC;
import dao.ProductoDAOJDBC;
import dao.UsuarioDAO;
import dao.UsuarioDAOJDBC;
import java.util.Scanner;
import modelo.Cliente;
import modelo.Pedido;
import modelo.Producto;

/**
 * Punto de entrada con PERSISTENCIA REAL en MySQL. A diferencia de Main
 * (que usa DAO en memoria), esta clase instancia los DAO JDBC, de modo
 * que toda lectura y escritura ocurre contra la base distribumax_db.
 *
 * Requisitos para ejecutar:
 *   1. Tener MySQL en marcha y la base creada con sql/distribumax_db.sql.
 *   2. Incluir el driver mysql-connector-j en el classpath.
 *
 * Gracias a que los controladores dependen de la interfaz GenericDAO y
 * no de una implementacion concreta (POLIMORFISMO), el unico cambio
 * respecto de la version en memoria es el tipo de DAO que se crea aqui.
 */
public class MainMySQL {

    public static void main(String[] args) {
        // --- DAO JDBC (capa de persistencia real) ---
        GenericDAO<Producto> productoDAO = new ProductoDAOJDBC();
        GenericDAO<Cliente> clienteDAO = new ClienteDAOJDBC();
        GenericDAO<Pedido> pedidoDAO = new PedidoDAOJDBC();
        UsuarioDAO usuarioDAO = new UsuarioDAOJDBC();

        // --- Controladores (capa de control) ---
        InventarioController inventario = new InventarioController(productoDAO);
        PedidoController pedidos = new PedidoController(pedidoDAO, productoDAO);

        // No se cargan datos: ya estan en la base (script distribumax_db.sql).

        // --- Vista (capa de presentacion) ---
        Scanner sc = new Scanner(System.in);
        try {
            MenuConsola menu = new MenuConsola(sc, inventario, pedidos, usuarioDAO, clienteDAO.listar());
            menu.iniciar();
        } catch (RuntimeException e) {
            System.out.println("No se pudo conectar con la base de datos.");
            System.out.println("Verifique que MySQL este en marcha, que exista la base");
            System.out.println("distribumax_db y que el driver JDBC este en el classpath.");
            System.out.println("Detalle: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
