package model;

import dao.AlquilerDAO;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Alquiler {

    private int id;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precioTotal;
    private Pago pago;

    private static AlquilerDAO dao = new AlquilerDAO();

    // Constructores
    public Alquiler() {
    }

    public Alquiler(int id, Cliente cliente, Vehiculo vehiculo,
            LocalDate fechaInicio, LocalDate fechaFin,
            double precioTotal, Pago pago) {
        this.id = id;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioTotal = precioTotal;
        this.pago = pago;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    // MÉTODOS DE INSTANCIA
    public void validar() throws Exception {
        if (cliente == null) {
            throw new Exception("El cliente es obligatorio");
        }
        if (vehiculo == null) {
            throw new Exception("El vehículo es obligatorio");
        }
        if (fechaInicio == null) {
            throw new Exception("La fecha de inicio es obligatoria");
        }
        if (fechaFin == null) {
            throw new Exception("La fecha de fin es obligatoria");
        }

        validarFechas(fechaInicio, fechaFin);

        if (precioTotal < 0) {
            throw new Exception("El precio total no puede ser negativo");
        }
    }

    private void validarFechas(LocalDate inicio, LocalDate fin) throws Exception {
        LocalDate hoy = LocalDate.now();

        if (inicio.isBefore(hoy)) {
            throw new Exception("La fecha de inicio no puede ser anterior a hoy");
        }

        if (fin.isBefore(inicio) || fin.isEqual(inicio)) {
            throw new Exception("La fecha de devolución debe ser posterior a la fecha de inicio");
        }

        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;
        if (dias > 365) {
            throw new Exception("El período de alquiler no puede exceder 365 días");
        }
    }

    public void guardar() throws Exception {
        validar();

        if (id == 0) {
            if (dao.vehiculoEstaAlquilado(vehiculo.getId(), fechaInicio, fechaFin)) {
                throw new Exception("El vehículo ya está alquilado en ese rango de fechas");
            }
            dao.agregar(this);
        } else {
            dao.actualizar(this);
        }
    }

    public void eliminar() {
        if (id > 0) {
            dao.eliminar(id);
        }
    }

    public long calcularDias() {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

    public double calcularPrecioBase() {
        return calcularDias() * vehiculo.getTarifaPorDia();
    }

    public double calcularKmIncluidos() {
        return calcularDias() * vehiculo.getKmIncluidoPorDia();
    }

    public Map<String, Double> calcularResumenDevolucion(double kmRecorridos) throws Exception {
        if (pago != null) {
            throw new Exception("Este alquiler ya fue devuelto");
        }

        if (kmRecorridos < 0) {
            throw new Exception("Los kilómetros recorridos no pueden ser negativos");
        }

        long dias = calcularDias();
        double kmIncluidos = calcularKmIncluidos();
        double kmExtra = Math.max(0, kmRecorridos - kmIncluidos);
        double costoExtra = kmExtra * vehiculo.getTarifaExtraPorKm();
        double precioBase = this.precioTotal;
        double total = precioBase + costoExtra;

        Map<String, Double> resumen = new HashMap<>();
        resumen.put("precioBase", precioBase);
        resumen.put("kmIncluidos", kmIncluidos);
        resumen.put("kmRecorridos", kmRecorridos);
        resumen.put("kmExtra", kmExtra);
        resumen.put("costoExtra", costoExtra);
        resumen.put("total", total);
        resumen.put("dias", (double) dias);

        return resumen;
    }

    public double procesarDevolucion(double kmRecorridos) throws Exception {
        if (pago != null) {
            throw new Exception("Este alquiler ya fue devuelto");
        }

        if (kmRecorridos < 0) {
            throw new Exception("Los kilómetros recorridos no pueden ser negativos");
        }

        double kmIncluidos = calcularKmIncluidos();
        double kmExtra = Math.max(0, kmRecorridos - kmIncluidos);
        double costoExtra = kmExtra * vehiculo.getTarifaExtraPorKm();

        this.precioTotal = calcularPrecioBase() + costoExtra;

        dao.actualizar(this);

        return costoExtra;
    }

    public void asociarPago(Pago pago) throws Exception {
        this.pago = pago;
        dao.actualizar(this);
    }

    public boolean estaDevuelto() {
        return pago != null;
    }

    public boolean estaPendiente() {
        return pago == null;
    }

    public boolean estaActivo() {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }

    public boolean haFinalizado() {
        return LocalDate.now().isAfter(fechaFin);
    }

    public long diasRestantes() {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaFin)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(hoy, fechaFin);
    }

    // MÉTODOS ESTÁTICOS    
    public static Alquiler crear(Cliente cliente, Vehiculo vehiculo,
            LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        if (dao.vehiculoEstaAlquilado(vehiculo.getId(), fechaInicio, fechaFin)) {
            throw new Exception("El vehículo ya está alquilado en ese rango de fechas");
        }

        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        double precioBase = dias * vehiculo.getTarifaPorDia();

        Alquiler alquiler = new Alquiler(0, cliente, vehiculo, fechaInicio, fechaFin,
                precioBase, null);
        alquiler.guardar();

        return alquiler;
    }

    public static double calcularPrecio(Vehiculo vehiculo, LocalDate inicio, LocalDate fin) {
        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;
        return dias * vehiculo.getTarifaPorDia();
    }

    public static List<Alquiler> obtenerTodos() {
        return dao.listar();
    }

    public static Alquiler buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public static void eliminarPorId(int id) {
        dao.eliminar(id);
    }

    
    public static List<Alquiler> obtenerActivos() {
        return dao.listar().stream()
                .filter(Alquiler::estaActivo)
                .toList();
    }

    public static List<Alquiler> obtenerPendientes() {
        return dao.listar().stream()
                .filter(Alquiler::estaPendiente)
                .toList();
    }

    public static List<Alquiler> obtenerDevueltos() {
        return dao.listar().stream()
                .filter(Alquiler::estaDevuelto)
                .toList();
    }

    public static List<Alquiler> obtenerPorCliente(int clienteId) {
        return dao.listar().stream()
                .filter(a -> a.getCliente().getId() == clienteId)
                .toList();
    }

    public static List<Alquiler> obtenerPorVehiculo(int vehiculoId) {
        return dao.listar().stream()
                .filter(a -> a.getVehiculo().getId() == vehiculoId)
                .toList();
    }

    public static boolean vehiculoDisponible(int vehiculoId, LocalDate inicio, LocalDate fin) {
        return !dao.vehiculoEstaAlquilado(vehiculoId, inicio, fin);
    }

    public static List<Vehiculo> obtenerVehiculosDisponibles() {
        List<Alquiler> pendientes = obtenerPendientes();

        return Vehiculo.obtenerTodos().stream()
                .filter(v -> {
                    boolean alquilado = pendientes.stream()
                            .anyMatch(a -> a.getVehiculo().getId() == v.getId());
                    return !alquilado;
                })
                .toList();
    }

    @Override
    public String toString() {
        return "Alquiler #" + id
                + " - Cliente: " + cliente.getNombre()
                + " - Vehículo: " + vehiculo.toString();
    }
}
