package distribumax.view;

import distribumax.controller.ProductoController;
import distribumax.model.Producto;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    private final ProductoController ctrl = new ProductoController();
    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {
        System.out.println("=== DistribuMax S.A. - Sistema de Gestion ===");
        int op;
        do {
            System.out.println("\n1. Listar productos");
            System.out.println("2. Agregar producto");
            System.out.println("3. Consultar stock");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            op = leerInt();
            switch (op) {
                case 1 -> listar();
                case 2 -> agregar();
                case 3 -> stock();
                case 0 -> System.out.println("Hasta luego!");
                default -> System.out.println("Opcion invalida.");
            }
        } while (op != 0);
    }

    private void listar() {
        List<Producto> lista = ctrl.obtenerTodos();
        if (lista.isEmpty()) { System.out.println("Sin productos."); return; }
        System.out.printf("%-5s %-25s %-10s%n", "ID", "Nombre", "Precio");
        System.out.println("-".repeat(42));
        lista.forEach(p -> System.out.printf("%-5d %-25s $%.2f%n",
            p.getId(), p.getNombre(), p.getPrecioUnitario()));
    }

    private void agregar() {
        System.out.print("Nombre: "); String nom = sc.nextLine();
        System.out.print("Descripcion: "); String des = sc.nextLine();
        System.out.print("Precio: "); double pre = leerDouble();
        System.out.print("Categoria (1=Almacen 2=Lacteos 3=Bebidas): "); int cat = leerInt();
        System.out.println(ctrl.registrarProducto(nom, des, pre, cat) ? "OK!" : "Error.");
    }

    private void stock() {
        System.out.print("ID producto: "); int id = leerInt();
        int s = ctrl.consultarStock(id);
        System.out.println("Stock: " + s + " unidades.");
        if (s <= 10) System.out.println("*** ALERTA: stock bajo minimo ***");
    }

    private int leerInt() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }
    private double leerDouble() {
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (Exception e) { return 0; }
    }
}
