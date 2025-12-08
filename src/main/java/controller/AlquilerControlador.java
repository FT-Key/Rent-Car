package controller;

import model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AlquilerControlador {

    private final VehiculoControlador vehiculoCtrl = new VehiculoControlador();

    public void crear(Cliente cliente, Vehiculo vehiculo,
            LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        Alquiler.crear(cliente, vehiculo, fechaInicio, fechaFin);
    }

    public double calcularPrecio(Vehiculo vehiculo, LocalDate inicio, LocalDate fin) {
        return Alquiler.calcularPrecio(vehiculo, inicio, fin);
    }

    public Map<String, Double> calcularResumenDevolucion(int alquilerId, double kmRecorridos) throws Exception {
        Alquiler alquiler = Alquiler.buscarPorId(alquilerId);
        if (alquiler == null) {
            throw new Exception("No existe el alquiler");
        }
        return alquiler.calcularResumenDevolucion(kmRecorridos);
    }

    public void procesarDevolucionCompleta(int alquilerId, double kmRecorridos,
            double montoTotal, String metodoPago) throws Exception {

        Alquiler alquiler = Alquiler.buscarPorId(alquilerId);
        if (alquiler == null) {
            throw new Exception("No existe el alquiler");
        }

        alquiler.procesarDevolucion(kmRecorridos);

        Pago.registrarPago(alquilerId, montoTotal, metodoPago);

        Vehiculo v = alquiler.getVehiculo();
        if (v == null) {
            return;
        }

        vehiculoCtrl.incrementarVecesAlquilado(v.getId());
    }

    public double devolverVehiculo(int alquilerId, double kmRecorridos) throws Exception {
        Alquiler alquiler = Alquiler.buscarPorId(alquilerId);
        if (alquiler == null) {
            throw new Exception("No existe el alquiler");
        }

        double costoExtra = alquiler.procesarDevolucion(kmRecorridos);

        return costoExtra;
    }

    public void asociarPago(int alquilerId, Pago pago) throws Exception {
        Alquiler alquiler = Alquiler.buscarPorId(alquilerId);
        if (alquiler == null) {
            throw new Exception("No existe el alquiler");
        }
        alquiler.asociarPago(pago);
    }

    public List<Alquiler> listar() {
        return Alquiler.obtenerTodos();
    }

    public List<Alquiler> listarActivos() {
        return Alquiler.obtenerActivos();
    }

    public List<Alquiler> listarPendientes() {
        return Alquiler.obtenerPendientes();
    }

    public List<Alquiler> listarDevueltos() {
        return Alquiler.obtenerDevueltos();
    }

    public List<Alquiler> listarPorCliente(int clienteId) {
        return Alquiler.obtenerPorCliente(clienteId);
    }

    public List<Alquiler> listarPorVehiculo(int vehiculoId) {
        return Alquiler.obtenerPorVehiculo(vehiculoId);
    }

    public List<Vehiculo> obtenerVehiculosDisponibles() {
        return Alquiler.obtenerVehiculosDisponibles();
    }

    public Alquiler buscar(int id) {
        return Alquiler.buscarPorId(id);
    }

    public void borrar(int id) {
        Alquiler.eliminarPorId(id);
    }

    public boolean verificarDisponibilidad(int vehiculoId, LocalDate inicio, LocalDate fin) {
        return Alquiler.vehiculoDisponible(vehiculoId, inicio, fin);
    }

    public double calcularPrecioEstimado(int vehiculoId, LocalDate inicio, LocalDate fin) {
        Vehiculo vehiculo = Vehiculo.buscarPorId(vehiculoId);
        if (vehiculo == null) {
            return 0.0;
        }
        return Alquiler.calcularPrecio(vehiculo, inicio, fin);
    }

    public String obtenerDetalles(int alquilerId) {
        Alquiler alquiler = Alquiler.buscarPorId(alquilerId);
        if (alquiler == null) {
            return "Alquiler no encontrado";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== DETALLES DEL ALQUILER ===\n");
        sb.append("ID: ").append(alquiler.getId()).append("\n");
        sb.append("Cliente: ").append(alquiler.getCliente().getNombre())
                .append(" ").append(alquiler.getCliente().getApellido()).append("\n");
        sb.append("Vehículo: ").append(alquiler.getVehiculo().toString()).append("\n");
        sb.append("Fecha inicio: ").append(alquiler.getFechaInicio()).append("\n");
        sb.append("Fecha fin: ").append(alquiler.getFechaFin()).append("\n");
        sb.append("Días: ").append(alquiler.calcularDias()).append("\n");
        sb.append("Km incluidos: ").append(alquiler.calcularKmIncluidos()).append("\n");
        sb.append("Precio total: $").append(String.format("%.2f", alquiler.getPrecioTotal())).append("\n");
        sb.append("Estado: ");
        if (alquiler.estaDevuelto()) {
            sb.append("DEVUELTO");
        } else if (alquiler.estaActivo()) {
            sb.append("ACTIVO (").append(alquiler.diasRestantes()).append(" días restantes)");
        } else if (alquiler.haFinalizado()) {
            sb.append("FINALIZADO - PENDIENTE DE DEVOLUCIÓN");
        } else {
            sb.append("RESERVADO");
        }

        return sb.toString();
    }
}
