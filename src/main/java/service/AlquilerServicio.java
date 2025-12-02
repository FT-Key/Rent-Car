package service;

import dao.AlquilerDAO;
import model.Alquiler;
import model.Cliente;
import model.Vehiculo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlquilerServicio {
    
    private AlquilerDAO dao = new AlquilerDAO();
    private VehiculoService vehiculoService = new VehiculoService();

    // CALCULAR PRECIO DE ALQUILER
    public double calcularPrecio(Vehiculo vehiculo, LocalDate inicio, LocalDate fin) {
        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;
        return dias * vehiculo.getTarifaPorDia();
    }

    // VALIDAR FECHAS
    public void validarFechas(LocalDate inicio, LocalDate fin) throws Exception {
        LocalDate hoy = LocalDate.now();
        
        if (inicio.isBefore(hoy)) {
            throw new Exception("La fecha de inicio no puede ser anterior a hoy.");
        }
        
        if (fin.isBefore(inicio) || fin.isEqual(inicio)) {
            throw new Exception("La fecha de devolución debe ser posterior a la fecha de inicio.");
        }
        
        // Validación adicional: máximo 365 días de alquiler
        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;
        if (dias > 365) {
            throw new Exception("El período de alquiler no puede exceder 365 días.");
        }
    }

    // CREAR ALQUILER
    public void crearAlquiler(Cliente cliente, Vehiculo vehiculo,
            LocalDate inicio, LocalDate fin) throws Exception {
        
        // Validar fechas
        validarFechas(inicio, fin);
        
        // Verificar disponibilidad
        if (dao.vehiculoEstaAlquilado(vehiculo.getId(), inicio, fin)) {
            throw new Exception("El vehículo ya está alquilado en ese rango de fechas.");
        }
        
        // Calcular precio
        double precioBase = calcularPrecio(vehiculo, inicio, fin);
        
        // Crear alquiler
        Alquiler alquiler = new Alquiler(
                0, cliente, vehiculo, inicio, fin, precioBase, null
        );
        
        dao.agregar(alquiler);
        
        // Actualizar contador del vehículo
        vehiculo.setVecesAlquilado(vehiculo.getVecesAlquilado() + 1);
        vehiculoService.actualizar(vehiculo);
    }

    // CALCULAR RESUMEN DE DEVOLUCIÓN
    public Map<String, Double> calcularResumenDevolucion(int alquilerId, double kmRecorridos) throws Exception {
        Alquiler alquiler = dao.buscarPorId(alquilerId);
        
        if (alquiler == null) {
            throw new Exception("No existe el alquiler.");
        }
        
        if (alquiler.getPago() != null) {
            throw new Exception("Este alquiler ya fue devuelto.");
        }
        
        Vehiculo v = alquiler.getVehiculo();
        
        // Calcular días
        long dias = ChronoUnit.DAYS.between(
            alquiler.getFechaInicio(), 
            alquiler.getFechaFin()
        ) + 1;
        
        // Calcular kilómetros incluidos
        double kmIncluidos = dias * v.getKmIncluidoPorDia();
        
        // Calcular kilómetros extra
        double kmExtra = Math.max(0, kmRecorridos - kmIncluidos);
        
        // Calcular costo extra
        double costoExtra = kmExtra * v.getTarifaExtraPorKm();
        
        // Precio base del alquiler
        double precioBase = alquiler.getPrecioTotal();
        
        // Total a cobrar
        double total = precioBase + costoExtra;
        
        // Retornar resumen
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

    // DEVOLVER VEHÍCULO
    public double procesarDevolucion(int alquilerId, double kmReal) throws Exception {
        Alquiler alquiler = dao.buscarPorId(alquilerId);
        
        if (alquiler == null) {
            throw new Exception("No existe el alquiler.");
        }
        
        if (alquiler.getPago() != null) {
            throw new Exception("Este alquiler ya fue devuelto.");
        }
        
        Vehiculo v = alquiler.getVehiculo();
        
        // Calcular días
        long dias = ChronoUnit.DAYS.between(
            alquiler.getFechaInicio(), 
            alquiler.getFechaFin()
        ) + 1;
        
        // Calcular kilómetros extra
        double kmIncluidos = dias * v.getKmIncluidoPorDia();
        double kmExtra = Math.max(0, kmReal - kmIncluidos);
        double costoExtra = kmExtra * v.getTarifaExtraPorKm();
        
        // Actualizar precio total final
        double precioFinal = alquiler.getPrecioTotal() + costoExtra;
        alquiler.setPrecioTotal(precioFinal);
        
        dao.actualizar(alquiler);
        
        return costoExtra;
    }

    // OBTENER VEHÍCULOS DISPONIBLES
    public List<Vehiculo> obtenerVehiculosDisponibles() {
        List<Alquiler> pendientes = dao.listar().stream()
                .filter(a -> a.getPago() == null)
                .toList();
        
        return vehiculoService.listar().stream()
                .filter(v -> {
                    boolean alquilado = pendientes.stream()
                            .anyMatch(a -> a.getVehiculo().getId() == v.getId());
                    return !alquilado;
                })
                .toList();
    }

    // CRUD BÁSICO
    public List<Alquiler> listar() {
        return dao.listar();
    }

    public Alquiler buscar(int id) {
        return dao.buscarPorId(id);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
    
    public List<Alquiler> listarPendientes() {
        return dao.listar().stream()
                .filter(a -> a.getPago() == null)
                .toList();
    }
    
    public List<Alquiler> listarDevueltos() {
        return dao.listar().stream()
                .filter(a -> a.getPago() != null)
                .toList();
    }
}