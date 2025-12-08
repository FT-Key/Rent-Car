package model;

import dao.VehiculoDAO;
import java.util.List;

public class Vehiculo {

    private int id;
    private String patente;
    private String modelo;
    private double kmIncluidoPorDia;
    private double tarifaPorDia;
    private double tarifaExtraPorKm;
    private int vecesAlquilado;

    private static VehiculoDAO dao = new VehiculoDAO();

    // Constructores
    public Vehiculo() {
    }

    public Vehiculo(int id, String patente, String modelo,
            double kmIncluidoPorDia, double tarifaPorDia, double tarifaExtraPorKm) {
        this.id = id;
        this.patente = patente;
        this.modelo = modelo;
        this.kmIncluidoPorDia = kmIncluidoPorDia;
        this.tarifaPorDia = tarifaPorDia;
        this.tarifaExtraPorKm = tarifaExtraPorKm;
        this.vecesAlquilado = 0;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getKmIncluidoPorDia() {
        return kmIncluidoPorDia;
    }

    public void setKmIncluidoPorDia(double kmIncluidoPorDia) {
        this.kmIncluidoPorDia = kmIncluidoPorDia;
    }

    public double getTarifaPorDia() {
        return tarifaPorDia;
    }

    public void setTarifaPorDia(double tarifaPorDia) {
        this.tarifaPorDia = tarifaPorDia;
    }

    public double getTarifaExtraPorKm() {
        return tarifaExtraPorKm;
    }

    public void setTarifaExtraPorKm(double tarifaExtraPorKm) {
        this.tarifaExtraPorKm = tarifaExtraPorKm;
    }

    public int getVecesAlquilado() {
        return vecesAlquilado;
    }

    public void setVecesAlquilado(int vecesAlquilado) {
        this.vecesAlquilado = vecesAlquilado;
    }

    // MÉTODOS DE INSTANCIA
    public void validar() throws Exception {
        if (patente == null || patente.isBlank()) {
            throw new Exception("La patente es obligatoria");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new Exception("El modelo es obligatorio");
        }
        if (kmIncluidoPorDia <= 0) {
            throw new Exception("Los kilómetros incluidos por día deben ser mayor a 0");
        }
        if (tarifaPorDia <= 0) {
            throw new Exception("La tarifa por día debe ser mayor a 0");
        }
        if (tarifaExtraPorKm < 0) {
            throw new Exception("La tarifa extra por km no puede ser negativa");
        }
    }

    public void guardar() throws Exception {
        validar();

        if (id == 0) {
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

    public double calcularCostoAlquiler(int dias, double kmRecorridos) {
        double costoBase = tarifaPorDia * dias;
        double kmIncluidos = kmIncluidoPorDia * dias;
        double kmExtra = Math.max(0, kmRecorridos - kmIncluidos);
        double costoExtra = kmExtra * tarifaExtraPorKm;

        return costoBase + costoExtra;
    }

    public void incrementarVecesAlquilado() {
        this.vecesAlquilado++;
    }

    public boolean estaDisponible() {
        return true;
    }

    // MÉTODOS ESTÁTICOS
    public static Vehiculo crear(String patente, String modelo, double kmIncluidoPorDia,
            double tarifaPorDia, double tarifaExtraPorKm) {
        return new Vehiculo(0, patente, modelo, kmIncluidoPorDia, tarifaPorDia, tarifaExtraPorKm);
    }

    public static List<Vehiculo> obtenerTodos() {
        return dao.listar();
    }

    public static Vehiculo buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public static void eliminarPorId(int id) {
        dao.eliminar(id);
    }

    public static List<Vehiculo> obtenerDisponibles() {
        return dao.listar();
    }

    @Override
    public String toString() {
        return modelo + " (" + patente + ")";
    }
}
