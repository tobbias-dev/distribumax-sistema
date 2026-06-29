package vista;

import java.util.List;
import java.util.Scanner;

import conexion.Conexion;
import dao.ClienteDAO;
import dao.DAOFactory;
import dao.PedidoDAO;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import excepciones.AccesoDatosException;
import excepciones.StockInsuficienteException;
import modelo.Pedido;
import modelo.Producto;
import modelo.Usuario;
import servicio.AuthService;
import servicio.InventarioService;
import servicio.PedidoService;
import servicio.ReporteArchivo;

/**
 * Punto de entrada y capa de vista del prototipo. Es un menú de consola: su
 * única responsabilidad es mostrar información y leer lo que escribe el
 * usuario. Toda la lógica vive en los servicios, y la persistencia en los DAO.
 *
 * El tipo de persistencia se elige por argumento de línea de comandos:
 *   java vista.Main           -> MySQL (por defecto)
 *   java vista.Main memoria   -> datos en memoria, sin servidor
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static InventarioService inventario;
    private static PedidoService pedidos;
    private static AuthService auth;
    private static ClienteDAO clienteDAO;
    private static DAOFactory.Tipo tipoPersistencia;

    public static void main(String[] args) {
        tipoPersistencia = DAOFactory.Tipo.MYSQL;
        if (args.length > 0 && args[0].equalsIgnoreCase("memoria")) {
            tipoPersistencia = DAOFactory.Tipo.MEMORIA;
        }

        System.out.println("Persistencia: " + tipoPersistencia);

        inicializar();

        try {
            Usuario usuario = login();
            if (usuario == null) {
                System.out.println("Acceso denegado. Se cierra la aplicación.");
                return;
            }
            System.out.println("Bienvenido/a, " + usuario.descripcion() + ".");
            menuPrincipal(usuario);
        } catch (AccesoDatosException e) {
            // Si la base no está disponible (servidor apagado o driver ausente),
            // informo de forma controlada en lugar de cortar con un error técnico.
            System.out.println("No se pudo trabajar con la base de datos: " + e.getMessage());
            System.out.println("Sugerencia: verificar que MySQL esté encendido, o ejecutar");
            System.out.println("con el argumento 'memoria' para correr sin servidor:");
            System.out.println("    java -cp bin vista.Main memoria");
        } finally {
            cerrar();
        }
    }

    /** Arma los DAO según el tipo elegido y se los inyecta a los servicios. */
    private static void inicializar() {
        DAOFactory factory = new DAOFactory(tipoPersistencia);
        ProductoDAO productoDAO = factory.getProductoDAO();
        UsuarioDAO usuarioDAO = factory.getUsuarioDAO();
        PedidoDAO pedidoDAO = factory.getPedidoDAO();
        clienteDAO = factory.getClienteDAO();

        inventario = new InventarioService(productoDAO);
        pedidos = new PedidoService(productoDAO, pedidoDAO);
        auth = new AuthService(usuarioDAO);
    }

    private static Usuario login() {
        System.out.print("Usuario: ");
        String u = sc.nextLine().trim();
        System.out.print("Contrasena: ");
        String p = sc.nextLine().trim();
        return auth.autenticar(u, p);
    }

    private static void menuPrincipal(Usuario usuario) {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();
            switch (opcion) {
                case "1": listarInventario(); break;
                case "2": verAlertas(); break;
                case "3": registrarPedido(usuario); break;
                case "4": ingresarMercaderia(); break;
                case "5": ordenarPorPrecio(); break;
                case "6": buscarProducto(); break;
                case "7": exportarReporte(); break;
                case "0": salir = true; break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("--------- MENU PRINCIPAL ---------");
        System.out.println(" 1. Listar inventario");
        System.out.println(" 2. Ver alertas de stock minimo");
        System.out.println(" 3. Registrar pedido (CU01)");
        System.out.println(" 4. Ingresar mercaderia");
        System.out.println(" 5. Listar productos por precio (orden)");
        System.out.println(" 6. Buscar producto por nombre");
        System.out.println(" 7. Exportar reporte de inventario a archivo");
        System.out.println(" 0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private static void listarInventario() {
        imprimirTabla(inventario.listarInventario());
    }

    private static void verAlertas() {
        List<Producto> alertas = inventario.listarAlertas();
        if (alertas.isEmpty()) {
            System.out.println("No hay productos por debajo del minimo.");
            return;
        }
        System.out.println("=== PRODUCTOS EN ALERTA (RF03) ===");
        imprimirTabla(alertas);
    }

    private static void registrarPedido(Usuario vendedor) {
        System.out.println("=== REGISTRAR PEDIDO ===");
        System.out.print("ID de cliente: ");
        int idCliente = leerEntero();
        if (clienteDAO.buscarPorId(idCliente) == null) {
            System.out.println("No existe ese cliente.");
            return;
        }

        Pedido pedido = pedidos.iniciarPedido(idCliente, vendedor.getId());

        boolean cargando = true;
        while (cargando) {
            imprimirTabla(inventario.listarInventario());
            System.out.print("ID de producto (0 para terminar): ");
            int idProd = leerEntero();
            if (idProd == 0) {
                cargando = false;
                continue;
            }
            System.out.print("Cantidad: ");
            int cant = leerEntero();
            try {
                pedidos.agregarItem(pedido, idProd, cant);
                System.out.println("  Agregado.");
            } catch (StockInsuficienteException e) {
                System.out.println("  No se pudo agregar: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("  Dato invalido: " + e.getMessage());
            }
        }

        if (pedido.estaVacio()) {
            System.out.println("Pedido vacio, no se registra.");
            return;
        }

        try {
            int id = pedidos.confirmarPedido(pedido);
            System.out.println();
            System.out.println("Pedido confirmado. ID: " + id + " | Estado: " + pedido.getEstado());
            System.out.printf("Total: $%.2f%n", pedido.getTotal());
        } catch (AccesoDatosException e) {
            System.out.println("Error al confirmar el pedido: " + e.getMessage());
        }
    }

    private static void ingresarMercaderia() {
        System.out.print("ID de producto: ");
        int idProd = leerEntero();
        System.out.print("Unidades a ingresar: ");
        int cant = leerEntero();
        try {
            inventario.ingresarMercaderia(idProd, cant);
            System.out.println("Stock actualizado.");
        } catch (IllegalArgumentException | AccesoDatosException e) {
            System.out.println("No se pudo actualizar: " + e.getMessage());
        }
    }

    private static void ordenarPorPrecio() {
        Producto[] ordenados = inventario.ordenarPorPrecio();
        System.out.println("=== CATALOGO ORDENADO POR PRECIO ===");
        System.out.printf("%-4s %-15s %-10s%n", "ID", "PRODUCTO", "PRECIO");
        for (Producto p : ordenados) {
            System.out.printf("%-4d %-15s %10.2f%n", p.getId(), p.getNombre(), p.getPrecioUnitario());
        }
    }

    private static void buscarProducto() {
        System.out.print("Nombre del producto: ");
        String nombre = sc.nextLine().trim();
        Producto p = inventario.buscarPorNombre(nombre);
        if (p == null) {
            System.out.println("No se encontro el producto.");
        } else {
            System.out.printf("Encontrado: %s | precio %.2f | stock %d%n",
                    p.getNombre(), p.getPrecioUnitario(), p.getStock().getCantidad());
        }
    }

    private static void exportarReporte() {
        ReporteArchivo reporte = new ReporteArchivo();
        String ruta = "reporte_inventario.txt";
        if (reporte.exportarInventario(inventario.listarInventario(), ruta)) {
            System.out.println("Reporte generado en: " + ruta);
        }
    }

    private static void imprimirTabla(List<Producto> productos) {
        System.out.println("=== INVENTARIO ACTUAL ===");
        System.out.printf("%-4s %-15s %-10s %-7s %-5s%n", "ID", "PRODUCTO", "PRECIO", "STOCK", "MIN");
        for (Producto p : productos) {
            System.out.printf("%-4d %-15s %-10.2f %-7d %-5d%n",
                    p.getId(), p.getNombre(), p.getPrecioUnitario(),
                    p.getStock().getCantidad(), p.getStock().getStockMinimo());
        }
    }

    private static int leerEntero() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void cerrar() {
        if (tipoPersistencia == DAOFactory.Tipo.MYSQL) {
            Conexion.getInstancia().cerrar();
        }
    }
}
