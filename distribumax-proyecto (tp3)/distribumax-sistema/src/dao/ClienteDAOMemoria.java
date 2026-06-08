package dao;

import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

/**
 * Implementacion en memoria del DAO de Cliente.
 */
public class ClienteDAOMemoria implements GenericDAO<Cliente> {

    private final List<Cliente> datos = new ArrayList<>();

    @Override
    public void insertar(Cliente cliente) {
        datos.add(cliente);
    }

    @Override
    public Cliente buscarPorId(int id) {
        for (Cliente c : datos) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> listar() {
        return new ArrayList<>(datos);
    }
}
