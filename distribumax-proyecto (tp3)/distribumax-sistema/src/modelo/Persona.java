package modelo;

/**
 * Clase abstracta que representa a una persona dentro del sistema.
 * Sirve como superclase para Usuario y Cliente, materializando el pilar
 * de ABSTRACCION (no tiene sentido instanciar "una persona" sin más) y
 * habilitando la HERENCIA hacia las clases concretas.
 *
 * El metodo descripcion() es abstracto: obliga a cada subclase a
 * definir su propia forma de presentarse (base para el POLIMORFISMO).
 */
public abstract class Persona {

    // Atributos privados -> ENCAPSULAMIENTO
    private int id;
    private String nombre;

    protected Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y setters: unico acceso permitido al estado interno
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo abstracto. Cada subclase decide como describirse.
     * Es el punto de extension polimorfico de la jerarquia.
     */
    public abstract String descripcion();
}
