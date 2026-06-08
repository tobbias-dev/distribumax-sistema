package vista;

import controlador.InventarioController;
import controlador.PedidoController;
import dao.ClienteDAOMemoria;
import dao.PedidoDAOMemoria;
import dao.ProductoDAOMemoria;
import dao.UsuarioDAOMemoria;
import java.util.Scanner;
import modelo.Categoria;
import modelo.Cliente;
import modelo.Rol;
import modelo.Usuario;

/**
 * Punto de entrada del prototipo. Crea los DAO, instancia los
 * controladores (inyeccion de dependencias por constructor), carga los
 * datos de prueba equivalentes a los INSERT del TP2 y lanza el menu.
 */
public class Main {

    public static void main(String[] args) {
        // --- DAO (capa de persistencia) ---
        ProductoDAOMemoria productoDAO = new ProductoDAOMemoria();
        ClienteDAOMemoria clienteDAO = new ClienteDAOMemoria();
        PedidoDAOMemoria pedidoDAO = new PedidoDAOMemoria();
        UsuarioDAOMemoria usuarioDAO = new UsuarioDAOMemoria();

        // --- Controladores (capa de control) ---
        InventarioController inventario = new InventarioController(productoDAO);
        PedidoController pedidos = new PedidoController(pedidoDAO, productoDAO);

        // --- Datos de prueba (espejo de los INSERT del TP2) ---
        Categoria almacen = new Categoria(1, "Almacen", "Productos secos de almacen");
        Categoria bebidas = new Categoria(2, "Bebidas", "Bebidas con y sin alcohol");
        Categoria limpieza = new Categoria(3, "Limpieza", "Productos de limpieza e higiene");

        inventario.altaProducto("Yerba 1kg", "Yerba mate molida", 1500.00, almacen, 85, 20);
        inventario.altaProducto("Aceite 1.5L", "Aceite de girasol", 2800.00, almacen, 12, 20);
        inventario.altaProducto("Fideos 500g", "Fideos guiseros", 650.00, almacen, 40, 15);
        inventario.altaProducto("Azucar 1kg", "Azucar comun", 950.00, almacen, 8, 25);
        inventario.altaProducto("Gaseosa 2.25L", "Gaseosa cola", 1800.00, bebidas, 60, 25);
        inventario.altaProducto("Lavandina 1L", "Lavandina concentrada", 900.00, limpieza, 30, 10);

        Cliente c1 = new Cliente(1, "Almacen El Sol", "Av. Illia 123", "2664-111111", "elsol@mail.com");
        clienteDAO.insertar(c1);
        clienteDAO.insertar(new Cliente(2, "Supermini Norte", "Junin 456", "2664-222222", "norte@mail.com"));

        usuarioDAO.insertar(new Usuario(1, "Administrador General", "admin",
            Usuario.hashSimple("admin123"), Rol.ADMIN));
        usuarioDAO.insertar(new Usuario(2, "Juan Perez", "vendedor1",
            Usuario.hashSimple("vend123"), Rol.VENDEDOR));

        // --- Vista (capa de presentacion) ---
        Scanner sc = new Scanner(System.in);
        MenuConsola menu = new MenuConsola(sc, inventario, pedidos, usuarioDAO, clienteDAO.listar());
        menu.iniciar();
        sc.close();
    }
}
