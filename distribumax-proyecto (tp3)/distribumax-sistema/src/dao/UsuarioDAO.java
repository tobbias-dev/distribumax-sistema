package dao;

import modelo.Usuario;

/**
 * Contrato especifico para el acceso a Usuario. Extiende GenericDAO y
 * agrega la busqueda por nombre de usuario, necesaria para autenticar.
 * Permite que la vista dependa de esta abstraccion y funcione igual con
 * la implementacion en memoria o con la JDBC (POLIMORFISMO).
 */
public interface UsuarioDAO extends GenericDAO<Usuario> {

    Usuario buscarPorUsuario(String nombreUsuario);
}
