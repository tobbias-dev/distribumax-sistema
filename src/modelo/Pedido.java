package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cabecera del pedido. Acá aparece el uso de ArrayList: las líneas del pedido
 * se guardan en una lista dinámica porque no sé de antemano cuántos productos
 * va a cargar el vendedor.
 */
public class Pedido {

    private int id;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private double total;
    private int idCliente;
    private int idVendedor;
    private List<DetallePedido> detalles;

    public Pedido(int idCliente, int idVendedor) {
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.detalles = new ArrayList<>();
        this.total = 0;
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        total += detalle.calcularSubtotal();
    }

    public double calcularTotal() {
        double t = 0;
        for (DetallePedido d : detalles) {
            t += d.calcularSubtotal();
        }
        total = t;
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public boolean estaVacio() {
        return detalles.isEmpty();
    }
}
