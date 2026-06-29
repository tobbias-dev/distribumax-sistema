package modelo;

/**
 * Cliente de DistribuMax (los comercios minoristas a los que abastece).
 * Hereda id y nombre de Persona, y suma los datos de contacto.
 */
public class Cliente extends Persona {

    private String direccion;
    private String telefono;
    private String email;

    public Cliente(int id, String nombre, String direccion, String telefono, String email) {
        super(id, nombre);
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String descripcion() {
        return getNombre() + " - " + (direccion != null ? direccion : "sin dirección");
    }
}
