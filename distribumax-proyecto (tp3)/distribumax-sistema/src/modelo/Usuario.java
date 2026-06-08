package modelo;

/**
 * Usuario del sistema. HEREDA de Persona y agrega credenciales y rol.
 * Sobrescribe descripcion() (POLIMORFISMO por sobreescritura).
 */
public class Usuario extends Persona {

    private String usuario;
    private String passwordHash;
    private Rol rol;

    public Usuario(int id, String nombre, String usuario, String passwordHash, Rol rol) {
        super(id, nombre); // invoca el constructor de la superclase
        this.usuario = usuario;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Verifica una contrasena contra el hash almacenado.
     * En el prototipo se usa el hashCode de String como sustituto del
     * algoritmo de hash definitivo (BCrypt en produccion, RNF08 del TP2).
     */
    public boolean autenticar(String passwordPlano) {
        if (passwordPlano == null) {
            return false;
        }
        return this.passwordHash.equals(hashSimple(passwordPlano));
    }

    public static String hashSimple(String texto) {
        return "h" + Integer.toHexString(texto.hashCode());
    }

    @Override
    public String descripcion() {
        return "Usuario [" + getNombre() + "] - rol: " + rol;
    }
}
