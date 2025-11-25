package controller;

import model.Cliente;
import service.ClienteServicio;

import java.util.List;

public class ClienteControlador {

    private ClienteServicio servicio;

    public ClienteControlador() {
        this.servicio = new ClienteServicio();
    }

    public void guardarCliente(String nombre, String apellido, String dni, String telefono,
                               String email, String direccion,
                               String licenciaNumero, String licenciaCategoria, String licenciaVencimiento)
            throws Exception {

        servicio.agregarCliente(nombre, apellido, dni, telefono, email, direccion,
                licenciaNumero, licenciaCategoria, licenciaVencimiento);
    }

    public void actualizarCliente(int id, String nombre, String apellido, String dni, String telefono,
                                  String email, String direccion,
                                  String licenciaNumero, String licenciaCategoria, String licenciaVencimiento)
            throws Exception {

        servicio.actualizarCliente(id, nombre, apellido, dni, telefono, email, direccion,
                licenciaNumero, licenciaCategoria, licenciaVencimiento);
    }

    public void borrarCliente(int id) {
        servicio.eliminarCliente(id);
    }

    public List<Cliente> obtenerClientes() {
        return servicio.listar();
    }
}
