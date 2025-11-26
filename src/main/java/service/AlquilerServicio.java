package service;

import dao.AlquilerDAO;
import model.Alquiler;
import model.Cliente;
import model.Vehiculo;
import model.Empleado;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AlquilerServicio {

    private AlquilerDAO dao = new AlquilerDAO();
    private VehiculoService vehiculoService = new VehiculoService();

    // ------------------------------
    // CREAR ALQUILER
    // ------------------------------
    public void crearAlquiler(Cliente cliente, Vehiculo vehiculo, Empleado empleado,
                              LocalDate inicio, LocalDate fin) throws Exception {

        if (inicio.isAfter(fin))
            throw new Exception("La fecha de inicio no puede ser posterior a la fecha de fin.");

        // 1️⃣ Verificar disponibilidad del vehículo
        if (dao.vehiculoEstaAlquilado(vehiculo.getId(), inicio, fin)) {
            throw new Exception("El vehículo ya está alquilado en ese rango de fechas.");
        }

        // 2️⃣ Calcular precio base
        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;
        double precioBase = dias * vehiculo.getTarifaPorDia();

        Alquiler alquiler = new Alquiler(
                0, cliente, vehiculo, empleado, inicio, fin, precioBase, null
        );

        dao.agregar(alquiler);

        // 3️⃣ Incrementar contador de veces alquilado
        vehiculo.setVecesAlquilado(vehiculo.getVecesAlquilado() + 1);
        vehiculoService.actualizar(vehiculo);
    }

    // ------------------------------
    // DEVOLVER VEHÍCULO
    // ------------------------------
    public double procesarDevolucion(int alquilerId, double kmReal) throws Exception {

        Alquiler alquiler = dao.buscarPorId(alquilerId);

        if (alquiler == null)
            throw new Exception("No existe el alquiler.");

        Vehiculo v = alquiler.getVehiculo();
        long dias = ChronoUnit.DAYS.between(alquiler.getFechaInicio(), alquiler.getFechaFin()) + 1;

        double kmIncluidos = dias * v.getKmIncluidoPorDia();

        double kmExtra = Math.max(0, kmReal - kmIncluidos);
        double costoExtra = kmExtra * v.getTarifaExtraPorKm();

        // actualizar precio total final
        double precioFinal = alquiler.getPrecioTotal() + costoExtra;
        alquiler.setPrecioTotal(precioFinal);

        dao.actualizar(alquiler);

        return costoExtra; // lo mostrás en pantalla para cobrarlo
    }

    // --------------------------------
    // CRUD BÁSICO
    // --------------------------------
    public List<Alquiler> listar() { return dao.listar(); }

    public Alquiler buscar(int id) { return dao.buscarPorId(id); }

    public void eliminar(int id) { dao.eliminar(id); }
}
