package model;

public class Vehiculo {

    private int id;
    private String patente;
    private String modelo;
    private double kmIncluidoPorDia;
    private double tarifaPorDia;
    private double tarifaExtraPorKm;
    private int vecesAlquilado;

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

    @Override
    public String toString() {
        return modelo + " (" + patente + ")";
    }

}
