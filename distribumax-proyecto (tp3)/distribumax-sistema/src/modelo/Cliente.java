package modelo;

/**
 * Cliente (comercio minorista atendido por DistribuMax).
 * HEREDA de Persona y sobrescribe descripcion() (POLIMORFISMO).
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

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String descripcion() {
        return "Cliente [" + getNombre() + "] - " + direccion;
    }
}
