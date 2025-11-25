package model;

import java.time.LocalDate;

public class Alquiler {

    private int id;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private Empleado empleado; // el que registra el alquiler
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precioTotal;
    private Pago pago; // puede ser null si aún no se pagó

    public Alquiler() {
    }

    public Alquiler(int id, Cliente cliente, Vehiculo vehiculo, Empleado empleado,
                    LocalDate fechaInicio, LocalDate fechaFin, double precioTotal, Pago pago) {

        this.id = id;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.empleado = empleado;
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

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
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

    @Override
    public String toString() {
        return "Alquiler #" + id +
                " - Cliente: " + cliente.getNombre() +
                " - Vehículo: " + vehiculo.toString();
    }
}
