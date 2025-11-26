package service;

import dao.EmpleadoDAO;
import model.Empleado;

import java.util.List;

public class EmpleadoServicio {

    private EmpleadoDAO dao;

    public EmpleadoServicio() {
        this.dao = new EmpleadoDAO();
    }

    public void agregarEmpleado(String nombre, String apellido, String dni, String telefono,
                                String email, String direccion,
                                String usuario, String password, String rol) throws Exception {

        if (nombre.isBlank() || apellido.isBlank() || dni.isBlank() ||
                telefono.isBlank() || email.isBlank() || direccion.isBlank() ||
                usuario.isBlank() || password.isBlank() || rol.isBlank()) {

            throw new Exception("Todos los campos son obligatorios");
        }

        Empleado e = new Empleado(
                0, nombre, apellido, dni, telefono, email, direccion,
                usuario, password, rol
        );

        dao.agregar(e);
    }

    public void actualizarEmpleado(int id, String nombre, String apellido, String dni, String telefono,
                                   String email, String direccion,
                                   String usuario, String password, String rol) throws Exception {

        if (nombre.isBlank() || apellido.isBlank() || dni.isBlank() ||
                telefono.isBlank() || email.isBlank() || direccion.isBlank() ||
                usuario.isBlank() || password.isBlank() || rol.isBlank()) {

            throw new Exception("Todos los campos son obligatorios");
        }

        Empleado e = new Empleado(
                id, nombre, apellido, dni, telefono, email, direccion,
                usuario, password, rol
        );

        dao.actualizar(e);
    }

    public void eliminarEmpleado(int id) {
        dao.eliminar(id);
    }

    public List<Empleado> listar() {
        return dao.listar();
    }
}
