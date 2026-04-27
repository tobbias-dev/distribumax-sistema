package distribumax.model;

import java.time.LocalDate;

public class Pedido {
    private int id;
    private LocalDate fecha;
    private String estado;
    private double total;
    private int idCliente;

    public Pedido() {}

    public Pedido(int id, LocalDate fecha, String estado, double total, int idCliente) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.idCliente = idCliente;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    @Override
    public String toString() { return "Pedido #" + id + " | " + estado + " | $" + total; }
}
