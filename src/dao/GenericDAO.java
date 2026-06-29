package dao;

import java.util.List;

/**
 * Interfaz genérica del patrón DAO (Data Access Object). Define el contrato
 * común de acceso a datos para cualquier entidad, sin atarse a una entidad
 * concreta gracias a los genéricos.
 *
 * Que los controladores dependan de esta interfaz y no de una implementación
 * concreta es lo que me permite cambiar la persistencia (MySQL o memoria) sin
 * tocar la lógica de negocio. Esa es la ventaja de diseño que buscaba desde
 * el TP3 y que en el TP4 termina de cobrar sentido.
 */
public interface GenericDAO<T> {

    void insertar(T entidad);

    T buscarPorId(int id);

    List<T> listar();
}
