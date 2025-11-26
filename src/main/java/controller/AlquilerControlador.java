package controller;

import model.*;
import service.AlquilerServicio;

import java.time.LocalDate;
import java.util.List;

public class AlquilerControlador {

    private AlquilerServicio service = new AlquilerServicio();

    public void crear(Cliente c, Vehiculo v, Empleado e,
                      LocalDate ini, LocalDate fin) throws Exception {

        service.crearAlquiler(c, v, e, ini, fin);
    }

    public double devolverVehiculo(int alquilerId, double kmFinal) throws Exception {
        return service.procesarDevolucion(alquilerId, kmFinal);
    }

    public List<Alquiler> listar() { return service.listar(); }

    public void borrar(int id) { service.eliminar(id); }
}
