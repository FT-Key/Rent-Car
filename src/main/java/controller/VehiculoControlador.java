package controller;

import model.Vehiculo;
import java.util.List;

public class VehiculoControlador {

    public void guardar(Vehiculo v) throws Exception {
        v.guardar();
    }

    public void guardar(String patente, String modelo, double kmIncluidoPorDia,
            double tarifaPorDia, double tarifaExtraPorKm) throws Exception {
        Vehiculo vehiculo = Vehiculo.crear(patente, modelo, kmIncluidoPorDia,
                tarifaPorDia, tarifaExtraPorKm);
        vehiculo.guardar();
    }

    public void actualizar(Vehiculo v) throws Exception {
        v.guardar();
    }

    public void borrar(int id) {
        Vehiculo.eliminarPorId(id);
    }

    public List<Vehiculo> listar() {
        return Vehiculo.obtenerTodos();
    }

    public Vehiculo buscar(int id) {
        return Vehiculo.buscarPorId(id);
    }

    public List<Vehiculo> listarDisponibles() {
        return Vehiculo.obtenerDisponibles();
    }

    public double calcularCosto(int idVehiculo, int dias, double kmRecorridos) {
        Vehiculo vehiculo = Vehiculo.buscarPorId(idVehiculo);
        if (vehiculo != null) {
            return vehiculo.calcularCostoAlquiler(dias, kmRecorridos);
        }
        return 0.0;
    }

    public void incrementarVecesAlquilado(int idVehiculo) {
        Vehiculo v = Vehiculo.buscarPorId(idVehiculo);
        if (v != null) {
            v.setVecesAlquilado(v.getVecesAlquilado() + 1);
            try {
                v.guardar();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
