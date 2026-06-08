package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cabecera de un pedido. Contiene sus lineas (DetallePedido) por
 * COMPOSICION: usa una LISTA ENLAZADA / dinamica de detalles, una de
 * las estructuras de datos sugeridas por la consigna.
 */
public class Pedido {

    private int id;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private Cliente cliente;
    private Usuario vendedor;
    private List<DetallePedido> detalles;

    public Pedido(int id, Cliente cliente, Usuario vendedor) {
        this.id = id;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.detalles = new ArrayList<>();
    }

    public int getId() {
        return id;
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

    public Cliente getCliente() {
        return cliente;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
    }

    /**
     * Recorre los detalles con un bucle (estructura repetitiva) y
     * acumula el total del pedido.
     */
    public double calcularTotal() {
        double total = 0;
        for (DetallePedido d : detalles) {
            total += d.calcularSubtotal();
        }
        return total;
    }
}
