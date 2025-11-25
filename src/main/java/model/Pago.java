package model;

import java.util.Date;

public class Pago {

    private int id;
    private int alquilerId;   // FK hacia alquiler
    private double monto;
    private String metodo;    // tarjeta, efectivo, transferencia
    private Date fecha;
    private String estado;    // pagado, pendiente

    public Pago() {}

    public Pago(int id, int alquilerId, double monto, String metodo, Date fecha, String estado) {
        this.id = id;
        this.alquilerId = alquilerId;
        this.monto = monto;
        this.metodo = metodo;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAlquilerId() { return alquilerId; }
    public void setAlquilerId(int alquilerId) { this.alquilerId = alquilerId; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
