package servicio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import modelo.Producto;

/**
 * Exportación del inventario a un archivo de texto. La consigna marca el uso
 * de archivos como opcional, pero lo incluyo porque cierra bien el reporte y
 * sirve de práctica para el EFIP. Muestra el manejo de E/S con try-with-
 * resources y captura de IOException.
 */
public class ReporteArchivo {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Escribe el inventario recibido en el archivo indicado. Devuelve true si
     * pudo escribir, false si hubo un error de E/S (informándolo).
     */
    public boolean exportarInventario(List<Producto> productos, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write("REPORTE DE INVENTARIO - DistribuMax S.A.");
            bw.newLine();
            bw.write("Generado: " + LocalDateTime.now().format(FMT));
            bw.newLine();
            bw.write("---------------------------------------------");
            bw.newLine();

            for (Producto p : productos) {
                String linea = String.format("%-15s | precio %8.2f | stock %3d | min %3d | %s",
                        p.getNombre(),
                        p.getPrecioUnitario(),
                        p.getStock().getCantidad(),
                        p.getStock().getStockMinimo(),
                        p.getStock().enAlerta() ? "ALERTA" : "OK");
                bw.write(linea);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("No se pudo generar el reporte: " + e.getMessage());
            return false;
        }
    }
}
