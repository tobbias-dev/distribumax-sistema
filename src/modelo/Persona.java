package modelo;

/**
 * Clase base abstracta que reúne lo común entre un Usuario y un Cliente:
 * un identificador y un nombre. No tiene sentido instanciar una persona
 * "genérica", así que la dejo abstracta y obligo a cada subclase a definir
 * cómo se describe a sí misma.
 *
 * Mantengo la misma decisión de diseño que venía del TP3, pero ahora la
 * uso de verdad: tanto Usuario como Cliente heredan de acá.
 */
public abstract class Persona {

    private int id;
    private String nombre;

    protected Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /** Cada subclase la resuelve a su manera (polimorfismo por sobreescritura). */
    public abstract String descripcion();
}
