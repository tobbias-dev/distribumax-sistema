package dao;

import java.util.List;

/**
 * Interfaz generica de acceso a datos. Define el contrato comun a todos
 * los DAO usando GENERICOS (<T>). Es una ABSTRACCION: el resto del
 * sistema depende de esta interfaz y no de una implementacion concreta,
 * lo que permite intercambiar la persistencia en memoria por JDBC/MySQL
 * sin tocar la logica de negocio (POLIMORFISMO por implementacion de
 * interfaz).
 */
public interface GenericDAO<T> {

    void insertar(T entidad);

    T buscarPorId(int id);

    List<T> listar();

    /**
     * Persiste los cambios de una entidad ya existente. En la version en
     * memoria no hace falta hacer nada (el objeto se modifica en el acto);
     * por eso se ofrece como metodo DEFAULT vacio. Los DAO JDBC lo
     * sobrescriben para ejecutar el UPDATE correspondiente en MySQL.
     */
    default void actualizar(T entidad) {
        // sin operacion en la implementacion en memoria
    }
}
