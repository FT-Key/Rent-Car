package controller;

import model.Vehiculo;
import service.VehiculoService;

import java.util.List;

public class VehiculoControlador {

    private VehiculoService service = new VehiculoService();

    public void guardar(Vehiculo v) { service.agregar(v); }

    public void actualizar(Vehiculo v) { service.actualizar(v); }

    public void borrar(int id) { service.eliminar(id); }

    public List<Vehiculo> listar() { return service.listar(); }
}
