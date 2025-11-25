package service;

import dao.ClienteDAO;
import model.Cliente;

import java.util.List;

public class ClienteServicio {

    private ClienteDAO dao;

    public ClienteServicio() {
        this.dao = new ClienteDAO();
    }

    public void agregarCliente(String nombre, String apellido, String dni, String telefono,
                               String email, String direccion,
                               String licenciaNumero, String licenciaCategoria, String licenciaVencimiento)
            throws Exception {

        if (nombre.isBlank() || apellido.isBlank() || dni.isBlank() ||
                telefono.isBlank() || email.isBlank() || direccion.isBlank() ||
                licenciaNumero.isBlank() || licenciaCategoria.isBlank() || licenciaVencimiento.isBlank()) {

            throw new Exception("Todos los campos son obligatorios");
        }

        Cliente c = new Cliente(
                0, nombre, apellido, dni, telefono, email, direccion,
                licenciaNumero, licenciaCategoria, licenciaVencimiento
        );

        dao.agregar(c);
    }

    public void actualizarCliente(int id, String nombre, String apellido, String dni, String telefono,
                                  String email, String direccion,
                                  String licenciaNumero, String licenciaCategoria, String licenciaVencimiento)
            throws Exception {

        if (nombre.isBlank() || apellido.isBlank() || dni.isBlank() ||
                telefono.isBlank() || email.isBlank() || direccion.isBlank() ||
                licenciaNumero.isBlank() || licenciaCategoria.isBlank() || licenciaVencimiento.isBlank()) {

            throw new Exception("Todos los campos son obligatorios");
        }

        Cliente c = new Cliente(
                id, nombre, apellido, dni, telefono, email, direccion,
                licenciaNumero, licenciaCategoria, licenciaVencimiento
        );

        dao.actualizar(c);
    }

    public void eliminarCliente(int id) {
        dao.eliminar(id);
    }

    public List<Cliente> listar() {
        return dao.listar();
    }
}
