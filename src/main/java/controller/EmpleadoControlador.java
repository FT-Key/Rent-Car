package controller;

import model.Empleado;
import service.EmpleadoServicio;

import java.util.List;

public class EmpleadoControlador {

    private EmpleadoServicio servicio;

    public EmpleadoControlador() {
        this.servicio = new EmpleadoServicio();
    }

    public void guardarEmpleado(String nombre, String apellido, String dni, String telefono,
                                String email, String direccion,
                                String usuario, String password, String rol) throws Exception {

        servicio.agregarEmpleado(nombre, apellido, dni, telefono, email, direccion,
                usuario, password, rol);
    }

    public void actualizarEmpleado(int id, String nombre, String apellido, String dni, String telefono,
                                   String email, String direccion,
                                   String usuario, String password, String rol) throws Exception {

        servicio.actualizarEmpleado(id, nombre, apellido, dni, telefono, email, direccion,
                usuario, password, rol);
    }

    public void borrarEmpleado(int id) {
        servicio.eliminarEmpleado(id);
    }

    public List<Empleado> obtenerEmpleados() {
        return servicio.listar();
    }
}
