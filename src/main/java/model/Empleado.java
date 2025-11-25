package model;

public class Empleado extends Persona {

    private String usuario;
    private String password;
    private String rol; // admin, vendedor, mecanico

    public Empleado() {
    }

    public Empleado(int id, String nombre, String apellido, String dni,
                    String telefono, String email, String direccion,
                    String usuario, String password, String rol) {

        super(id, nombre, apellido, dni, telefono, email, direccion);
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
