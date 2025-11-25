package service;

import dao.VehiculoDAO;
import model.Vehiculo;

import java.util.List;

public class VehiculoService {

    private VehiculoDAO dao = new VehiculoDAO();

    public void agregar(Vehiculo v) { dao.agregar(v); }

    public void actualizar(Vehiculo v) { dao.actualizar(v); }

    public void eliminar(int id) { dao.eliminar(id); }

    public Vehiculo buscar(int id) { return dao.buscarPorId(id); }

    public List<Vehiculo> listar() { return dao.listar(); }
}
