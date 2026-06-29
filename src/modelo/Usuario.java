package modelo;

/**
 * Usuario del sistema. Hereda de Persona el id y el nombre, y agrega
 * las credenciales y el rol. La autenticación es deliberadamente simple
 * (comparación de hash) porque el foco del TP está en la persistencia y
 * los pilares de POO, no en criptografía.
 */
public class Usuario extends Persona {

    private String usuario;
    private String passwordHash;
    private Rol rol;

    public Usuario(int id, String nombre, String usuario, String passwordHash, Rol rol) {
        super(id, nombre);
        this.usuario = usuario;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    /**
     * Valida la contraseña recibida contra el hash guardado. Mantengo el
     * mismo esquema de "hash" de prueba que ya traía la base del TP3.
     */
    public boolean autenticar(String passwordPlano) {
        return passwordHash != null && passwordHash.equals(Hash.de(passwordPlano));
    }

    @Override
    public String descripcion() {
        return getNombre() + " (" + rol + ")";
    }
}
