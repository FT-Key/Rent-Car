package controller;

import model.*;
import service.AlquilerServicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AlquilerControlador {

    private AlquilerServicio service = new AlquilerServicio();

    public void crear(Cliente cliente, Vehiculo vehiculo,
            LocalDate ini, LocalDate fin) throws Exception {
        service.crearAlquiler(cliente, vehiculo, ini, fin);
    }

    public Map<String, Double> calcularResumenDevolucion(int alquilerId, double kmRecorridos) throws Exception {
        return service.calcularResumenDevolucion(alquilerId, kmRecorridos);
    }

    public double devolverVehiculo(int alquilerId, double kmFinal) throws Exception {
        return service.procesarDevolucion(alquilerId, kmFinal);
    }

    public double calcularPrecio(Vehiculo vehiculo, LocalDate inicio, LocalDate fin) {
        return service.calcularPrecio(vehiculo, inicio, fin);
    }

    public List<Vehiculo> obtenerVehiculosDisponibles() {
        return service.obtenerVehiculosDisponibles();
    }

    public List<Alquiler> listar() {
        return service.listar();
    }

    public List<Alquiler> listarPendientes() {
        return service.listarPendientes();
    }

    public List<Alquiler> listarDevueltos() {
        return service.listarDevueltos();
    }

    public Alquiler buscar(int id) {
        return service.buscar(id);
    }

    public void borrar(int id) {
        service.eliminar(id);
    }
}
