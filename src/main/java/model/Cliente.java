package model;

public class Cliente extends Persona {

    private String licenciaNumero;
    private String licenciaCategoria;
    private String licenciaVencimiento;

    public Cliente() {
    }

    public Cliente(int id, String nombre, String apellido, String dni, String telefono,
                   String email, String direccion, String licenciaNumero,
                   String licenciaCategoria, String licenciaVencimiento) {

        super(id, nombre, apellido, dni, telefono, email, direccion);
        this.licenciaNumero = licenciaNumero;
        this.licenciaCategoria = licenciaCategoria;
        this.licenciaVencimiento = licenciaVencimiento;
    }

    // Getters y Setters
    public String getLicenciaNumero() { return licenciaNumero; }
    public void setLicenciaNumero(String licenciaNumero) { this.licenciaNumero = licenciaNumero; }

    public String getLicenciaCategoria() { return licenciaCategoria; }
    public void setLicenciaCategoria(String licenciaCategoria) { this.licenciaCategoria = licenciaCategoria; }

    public String getLicenciaVencimiento() { return licenciaVencimiento; }
    public void setLicenciaVencimiento(String licenciaVencimiento) { this.licenciaVencimiento = licenciaVencimiento; }
}
