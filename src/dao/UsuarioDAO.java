package dao;

import modelo.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario> {

    /** Busca un usuario por su nombre de login, para la autenticación. */
    Usuario buscarPorUsuario(String usuario);
}
