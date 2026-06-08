package dao;

import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

/**
 * Implementacion en memoria del DAO de Usuario. Agrega un metodo de
 * busqueda por nombre de usuario, necesario para la autenticacion.
 */
public class UsuarioDAOMemoria implements UsuarioDAO {

    private final List<Usuario> datos = new ArrayList<>();

    @Override
    public void insertar(Usuario usuario) {
        datos.add(usuario);
    }

    @Override
    public Usuario buscarPorId(int id) {
        for (Usuario u : datos) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    public Usuario buscarPorUsuario(String nombreUsuario) {
        for (Usuario u : datos) {
            if (u.getUsuario().equalsIgnoreCase(nombreUsuario)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(datos);
    }
}
