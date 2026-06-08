package vista;

import controlador.InventarioController;
import controlador.PedidoController;
import dao.UsuarioDAO;
import java.util.List;
import java.util.Scanner;
import modelo.Cliente;
import modelo.DetallePedido;
import modelo.Pedido;
import modelo.Producto;
import modelo.StockInsuficienteException;
import modelo.Usuario;

/**
 * Vista de consola del sistema. Presenta el MENU DE SELECCION exigido
 * por la consigna y delega toda la logica en los controladores (patron
 * MVC). Aplica estructuras condicionales (switch/if) y repetitivas
 * (while) para controlar el flujo, y maneja las excepciones que
 * propagan las capas inferiores.
 */
public class MenuConsola {

    private final Scanner sc;
    private final InventarioController inventario;
    private final PedidoController pedidos;
    private final UsuarioDAO usuarioDAO;
    private final List<Cliente> clientes;
    private Usuario usuarioActual;

    public MenuConsola(Scanner sc, InventarioController inventario, PedidoController pedidos,
                       UsuarioDAO usuarioDAO, List<Cliente> clientes) {
        this.sc = sc;
        this.inventario = inventario;
        this.pedidos = pedidos;
        this.usuarioDAO = usuarioDAO;
        this.clientes = clientes;
    }

    public void iniciar() {
        System.out.println("============================================");
        System.out.println(" DistribuMax S.A. - Sistema de Gestion");
        System.out.println(" Inventario y Logistica (prototipo TP3)");
        System.out.println("============================================");

        if (!autenticar()) {
            System.out.println("Acceso denegado. Fin del programa.");
            return;
        }

        boolean salir = false;
        while (!salir) { // estructura repetitiva: bucle principal del menu
            mostrarMenu();
            String opcion = sc.nextLine().trim();
            switch (opcion) { // estructura condicional multiple
                case "1":
                    listarInventario();
                    break;
                case "2":
                    mostrarAlertas();
                    break;
                case "3":
                    registrarPedido();
                    break;
                case "4":
                    ingresarMercaderia();
                    break;
                case "5":
                    listarOrdenadoPorPrecio();
                    break;
                case "6":
                    buscarProducto();
                    break;
                case "0":
                    salir = true;
                    System.out.println("Sesion finalizada. Hasta luego.");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }
        }
    }

    private boolean autenticar() {
        System.out.print("\nUsuario: ");
        String user = sc.nextLine().trim();
        System.out.print("Contrasena: ");
        String pass = sc.nextLine().trim();

        Usuario u = usuarioDAO.buscarPorUsuario(user);
        if (u != null && u.autenticar(pass)) {
            usuarioActual = u;
            System.out.println("Bienvenido/a, " + u.getNombre() + " (" + u.getRol() + ").");
            return true;
        }
        System.out.println("Usuario o contrasena incorrectos.");
        return false;
    }

    private void mostrarMenu() {
        System.out.println("\n--------- MENU PRINCIPAL ---------");
        System.out.println(" 1. Listar inventario");
        System.out.println(" 2. Ver alertas de stock minimo");
        System.out.println(" 3. Registrar pedido (CU01)");
        System.out.println(" 4. Ingresar mercaderia");
        System.out.println(" 5. Listar productos por precio (orden)");
        System.out.println(" 6. Buscar producto por nombre");
        System.out.println(" 0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private void listarInventario() {
        System.out.println("\n=== INVENTARIO ACTUAL ===");
        System.out.printf("%-4s %-16s %-10s %-8s %-8s%n", "ID", "PRODUCTO", "PRECIO", "STOCK", "MIN");
        for (Producto p : inventario.listarProductos()) {
            System.out.printf("%-4d %-16s %-10.2f %-8d %-8d%n",
                p.getId(), p.getNombre(), p.getPrecioUnitario(),
                p.getStock().getCantidad(), p.getStock().getStockMinimo());
        }
    }

    private void mostrarAlertas() {
        System.out.println("\n=== ALERTAS DE STOCK (RF03) ===");
        List<Producto> alertas = inventario.productosEnAlerta();
        if (alertas.isEmpty()) {
            System.out.println("No hay productos por debajo del minimo.");
            return;
        }
        for (Producto p : alertas) {
            System.out.println("  [ALERTA] " + p.getNombre()
                + " -> disponible " + p.getStock().getCantidad()
                + " (minimo " + p.getStock().getStockMinimo() + ")");
        }
    }

    private void registrarPedido() {
        System.out.println("\n=== REGISTRAR PEDIDO (CU01) ===");
        Cliente cliente = clientes.get(0); // simplificacion para la demo
        System.out.println("Cliente: " + cliente.getNombre());

        Pedido pedido = pedidos.nuevoPedido(cliente, usuarioActual);
        boolean agregando = true;
        while (agregando) {
            listarInventario();
            System.out.print("ID de producto (0 para terminar): ");
            int idProd = leerEntero();
            if (idProd == 0) {
                agregando = false;
                continue;
            }
            Producto p = inventario.buscarPorId(idProd);
            if (p == null) {
                System.out.println("Producto inexistente.");
                continue;
            }
            System.out.print("Cantidad: ");
            int cant = leerEntero();
            try {
                pedidos.agregarItem(pedido, p, cant);
                System.out.println("  Agregado: " + cant + " x " + p.getNombre());
            } catch (StockInsuficienteException e) {
                // manejo de la excepcion de dominio
                System.out.println("  No se pudo agregar: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("  Dato invalido: " + e.getMessage());
            }
        }

        try {
            pedidos.confirmar(pedido);
            System.out.println("\nPedido confirmado. Estado: " + pedido.getEstado());
            System.out.printf("Total: $%.2f%n", pedido.calcularTotal());
            for (DetallePedido d : pedido.getDetalles()) {
                System.out.printf("   - %s x%d = $%.2f%n",
                    d.getProducto().getNombre(), d.getCantidad(), d.calcularSubtotal());
            }
        } catch (IllegalStateException e) {
            System.out.println("No se confirmo el pedido: " + e.getMessage());
        }
    }

    private void ingresarMercaderia() {
        System.out.println("\n=== INGRESO DE MERCADERIA ===");
        System.out.print("ID de producto: ");
        int id = leerEntero();
        System.out.print("Cantidad a ingresar: ");
        int cant = leerEntero();
        try {
            inventario.ingresarMercaderia(id, cant);
            System.out.println("Stock actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarOrdenadoPorPrecio() {
        System.out.println("\n=== PRODUCTOS ORDENADOS POR PRECIO (insertion sort) ===");
        for (Producto p : inventario.ordenarPorPrecio()) {
            System.out.printf("  $%-10.2f %s%n", p.getPrecioUnitario(), p.getNombre());
        }
    }

    private void buscarProducto() {
        System.out.print("\nTexto a buscar: ");
        String texto = sc.nextLine().trim();
        List<Producto> r = inventario.buscarPorNombre(texto);
        if (r.isEmpty()) {
            System.out.println("Sin coincidencias.");
            return;
        }
        for (Producto p : r) {
            System.out.println("  -> " + p.getNombre() + " (stock " + p.getStock().getCantidad() + ")");
        }
    }

    private int leerEntero() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // valor invalido controlado
        }
    }
}
