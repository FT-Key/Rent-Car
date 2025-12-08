package controller;

import model.Cliente;
import java.util.List;

public class ClienteControlador {

    /**
     * Guarda un nuevo cliente en la base de datos
     */
    public void guardarCliente(String nombre, String apellido, String dni, String telefono,
            String email, String direccion,
            String licenciaNumero, String licenciaCategoria, String licenciaVencimiento)
            throws Exception {
        Cliente cliente = Cliente.crear(nombre, apellido, dni, telefono, email, direccion,
                licenciaNumero, licenciaCategoria, licenciaVencimiento);
        cliente.guardar();
    }

    /**
     * Actualiza un cliente existente
     */
    public void actualizarCliente(int id, String nombre, String apellido, String dni, String telefono,
            String email, String direccion,
            String licenciaNumero, String licenciaCategoria, String licenciaVencimiento)
            throws Exception {
        Cliente cliente = new Cliente(id, nombre, apellido, dni, telefono, email, direccion,
                licenciaNumero, licenciaCategoria, licenciaVencimiento);
        cliente.guardar();
    }

    /**
     * Elimina un cliente por su ID
     */
    public void borrarCliente(int id) {
        Cliente.eliminarPorId(id);
    }

    /**
     * Obtiene todos los clientes
     */
    public List<Cliente> obtenerClientes() {
        return Cliente.obtenerTodos();
    }

    /**
     * Busca un cliente por su ID
     */
    public Cliente buscarCliente(int id) {
        return Cliente.buscarPorId(id);
    }
}
