package servicio;

import dao.UsuarioDAO;
import modelo.Usuario;

/**
 * Servicio de autenticación. Busca el usuario en la base por su login y
 * valida la contraseña. Devuelve el Usuario si las credenciales son correctas
 * o null si no lo son.
 */
public class AuthService {

    private final UsuarioDAO usuarioDAO;

    public AuthService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario autenticar(String nombreUsuario, String password) {
        Usuario u = usuarioDAO.buscarPorUsuario(nombreUsuario);
        if (u != null && u.autenticar(password)) {
            return u;
        }
        return null;
    }
}
