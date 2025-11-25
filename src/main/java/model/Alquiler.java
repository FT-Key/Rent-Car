package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Alquiler {
    private int id;
    private Vehiculo vehiculo;
    private Cliente cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int kmInicio;
    private int kmFin;
    private double montoTotal;
    private boolean devuelto;

    public Alquiler() {
        this.devuelto = false;
    }

    public Alquiler(int id, Vehiculo vehiculo, Cliente cliente,
                    LocalDate fechaInicio, LocalDate fechaFin, int kmInicio, int kmFin, boolean devuelto) {
        this.id = id;
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.kmInicio = kmInicio;
        this.kmFin = kmFin;
        this.devuelto = devuelto;
    }

    // Cálculos de negocio
    public int getDias() {
        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        return (int) Math.max(dias, 1);
    }

    public int getKmRecorridos() {
        return kmFin - kmInicio;
    }

    public double getKmPermitidos() {
        return vehiculo.getKmIncluidoPorDia() * getDias();
    }

    public double getKmExtras() {
        double extras = getKmRecorridos() - getKmPermitidos();
        return Math.max(0, extras);
    }

    public double calcularMontoTotal() {
        double base = vehiculo.getTarifaPorDia() * getDias();
        double extra = getKmExtras() * vehiculo.getTarifaExtraPorKm();
        montoTotal = base + extra;
        return montoTotal;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public int getKmInicio() { return kmInicio; }
    public void setKmInicio(int kmInicio) { this.kmInicio = kmInicio; }

    public int getKmFin() { return kmFin; }
    public void setKmFin(int kmFin) { this.kmFin = kmFin; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public boolean isDevuelto() { return devuelto; }
    public void setDevuelto(boolean devuelto) { this.devuelto = devuelto; }
}
