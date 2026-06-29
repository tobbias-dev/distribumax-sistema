package dao;

import java.util.ArrayList;
import java.util.List;

import modelo.Cliente;

public class ClienteDAOMemoria implements ClienteDAO {

    private final List<Cliente> clientes = new ArrayList<>();

    public ClienteDAOMemoria() {
        clientes.add(new Cliente(1, "Almacen El Sol", "Av. Illia 123", "2664-111111", "elsol@mail.com"));
        clientes.add(new Cliente(2, "Supermini Norte", "Junin 456", "2664-222222", "norte@mail.com"));
    }

    @Override
    public Cliente buscarPorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> listar() {
        return new ArrayList<>(clientes);
    }

    @Override
    public void insertar(Cliente cliente) {
        clientes.add(cliente);
    }
}
