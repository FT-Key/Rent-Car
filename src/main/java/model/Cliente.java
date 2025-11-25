package model;

public class Cliente {

    private int id;
    private String nombre;
    private String licenciaConducir;

    public Cliente() {
    }

    public Cliente(int id, String nombre, String licenciaConducir) {
        this.id = id;
        this.nombre = nombre;
        this.licenciaConducir = licenciaConducir;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLicenciaConducir() {
        return licenciaConducir;
    }

    public void setLicenciaConducir(String licenciaConducir) {
        this.licenciaConducir = licenciaConducir;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
