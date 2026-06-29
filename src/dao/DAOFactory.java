package dao;

/**
 * Fábrica de DAOs. Centraliza la decisión de qué implementación usar (MySQL
 * por JDBC o memoria) y devuelve siempre el tipo de la interfaz, nunca el de
 * la clase concreta. Así, cambiar de una persistencia a la otra es cuestión de
 * un solo parámetro en el arranque, sin tocar la lógica de negocio.
 *
 * Es un complemento natural del patrón DAO: el resto del sistema pide "un
 * ProductoDAO" y no le importa cuál.
 */
public class DAOFactory {

    public enum Tipo {
        MYSQL,
        MEMORIA
    }

    private final Tipo tipo;

    public DAOFactory(Tipo tipo) {
        this.tipo = tipo;
    }

    public ProductoDAO getProductoDAO() {
        return (tipo == Tipo.MYSQL) ? new ProductoDAOJDBC() : new ProductoDAOMemoria();
    }

    public UsuarioDAO getUsuarioDAO() {
        return (tipo == Tipo.MYSQL) ? new UsuarioDAOJDBC() : new UsuarioDAOMemoria();
    }

    public ClienteDAO getClienteDAO() {
        return (tipo == Tipo.MYSQL) ? new ClienteDAOJDBC() : new ClienteDAOMemoria();
    }

    public PedidoDAO getPedidoDAO() {
        return (tipo == Tipo.MYSQL) ? new PedidoDAOJDBC() : new PedidoDAOMemoria();
    }
}
