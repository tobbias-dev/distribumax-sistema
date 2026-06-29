package dao;

import java.util.ArrayList;
import java.util.List;

import modelo.Hash;
import modelo.Rol;
import modelo.Usuario;

public class UsuarioDAOMemoria implements UsuarioDAO {

    private final List<Usuario> usuarios = new ArrayList<>();

    public UsuarioDAOMemoria() {
        // Mismas credenciales de prueba que el script SQL.
        usuarios.add(new Usuario(1, "Administrador General", "admin",
                Hash.de("admin123"), Rol.ADMIN));
        usuarios.add(new Usuario(2, "Juan Perez", "vendedor1",
                Hash.de("vend123"), Rol.VENDEDOR));
        usuarios.add(new Usuario(3, "Carlos Ruiz", "chofer1",
                Hash.de("chof123"), Rol.TRANSPORTISTA));
    }

    @Override
    public Usuario buscarPorUsuario(String usuario) {
        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(usuario)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public Usuario buscarPorId(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public void insertar(Usuario usuario) {
        usuarios.add(usuario);
    }
}
