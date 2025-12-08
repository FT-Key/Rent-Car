package model;

import dao.EmpleadoDAO;
import java.util.List;

public class Empleado extends Persona {
    private String usuario;
    private String password;
    private String rol;
    
    private static EmpleadoDAO dao = new EmpleadoDAO();
    
    // Constantes para roles
    public static final String ROL_ADMIN = "admin";
    public static final String ROL_VENDEDOR = "vendedor";
    public static final String ROL_MECANICO = "mecanico";
    
    // Constructores
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
    
    // MÉTODOS DE INSTANCIA
    public void validar() throws Exception {
        // Validar campos heredados de Persona
        if (getNombre() == null || getNombre().isBlank()) {
            throw new Exception("El nombre es obligatorio");
        }
        if (getApellido() == null || getApellido().isBlank()) {
            throw new Exception("El apellido es obligatorio");
        }
        if (getDni() == null || getDni().isBlank()) {
            throw new Exception("El DNI es obligatorio");
        }
        if (getTelefono() == null || getTelefono().isBlank()) {
            throw new Exception("El teléfono es obligatorio");
        }
        if (getEmail() == null || getEmail().isBlank()) {
            throw new Exception("El email es obligatorio");
        }
        if (getDireccion() == null || getDireccion().isBlank()) {
            throw new Exception("La dirección es obligatoria");
        }
        
        // Validar campos específicos de Empleado
        if (usuario == null || usuario.isBlank()) {
            throw new Exception("El usuario es obligatorio");
        }
        if (usuario.length() < 4) {
            throw new Exception("El usuario debe tener al menos 4 caracteres");
        }
        if (password == null || password.isBlank()) {
            throw new Exception("La contraseña es obligatoria");
        }
        if (password.length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres");
        }
        if (rol == null || rol.isBlank()) {
            throw new Exception("El rol es obligatorio");
        }
        if (!esRolValido(rol)) {
            throw new Exception("Rol inválido. Use: admin, vendedor o mecanico");
        }
    }
    
    public void guardar() throws Exception {
        validar();
        
        if (getId() == 0) {
            if (usuarioExiste(usuario)) {
                throw new Exception("El usuario ya existe");
            }
            dao.agregar(this);
        } else {
            dao.actualizar(this);
        }
    }
    
    public void eliminar() {
        if (getId() > 0) {
            dao.eliminar(getId());
        }
    }
    
    public boolean verificarPassword(String passwordIngresada) {
        return this.password != null && this.password.equals(passwordIngresada);
    }
    
    public void cambiarPassword(String passwordActual, String passwordNueva) throws Exception {
        if (!verificarPassword(passwordActual)) {
            throw new Exception("La contraseña actual es incorrecta");
        }
        if (passwordNueva == null || passwordNueva.length() < 6) {
            throw new Exception("La nueva contraseña debe tener al menos 6 caracteres");
        }
        this.password = passwordNueva;
        dao.actualizar(this);
    }
    
    public boolean esAdmin() {
        return ROL_ADMIN.equalsIgnoreCase(rol);
    }
    
    public boolean esVendedor() {
        return ROL_VENDEDOR.equalsIgnoreCase(rol);
    }
    
    public boolean esMecanico() {
        return ROL_MECANICO.equalsIgnoreCase(rol);
    }
    
    private boolean esRolValido(String rol) {
        return ROL_ADMIN.equalsIgnoreCase(rol) ||
               ROL_VENDEDOR.equalsIgnoreCase(rol) ||
               ROL_MECANICO.equalsIgnoreCase(rol);
    }
    
    public boolean tienePermisosAdmin() {
        return esAdmin();
    }
    
    public boolean puedeRegistrarAlquileres() {
        return esAdmin() || esVendedor();
    }
    
    public boolean puedeGestionarVehiculos() {
        return esAdmin() || esMecanico();
    }
    
    public String getNombreCompletoConRol() {
        return getNombre() + " " + getApellido() + " (" + rol + ")";
    }
    
    // MÉTODOS ESTÁTICOS
    
    public static Empleado crear(String nombre, String apellido, String dni,
                                 String telefono, String email, String direccion,
                                 String usuario, String password, String rol) {
        return new Empleado(0, nombre, apellido, dni, telefono, email, direccion,
                           usuario, password, rol);
    }
    
    public static List<Empleado> obtenerTodos() {
        return dao.listar();
    }
    
    public static Empleado buscarPorId(int id) {
        return dao.buscarPorId(id);
    }
    
    public static Empleado buscarPorUsuario(String usuario) {
        return dao.listar().stream()
                .filter(e -> e.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }
    
    public static Empleado autenticar(String usuario, String password) {
        Empleado empleado = buscarPorUsuario(usuario);
        if (empleado != null && empleado.verificarPassword(password)) {
            return empleado;
        }
        return null;
    }
    
    public static boolean usuarioExiste(String usuario) {
        return buscarPorUsuario(usuario) != null;
    }
    
    public static List<Empleado> obtenerPorRol(String rol) {
        return dao.listar().stream()
                .filter(e -> e.getRol().equalsIgnoreCase(rol))
                .toList();
    }
    
    public static List<Empleado> obtenerAdministradores() {
        return obtenerPorRol(ROL_ADMIN);
    }
    
    public static List<Empleado> obtenerVendedores() {
        return obtenerPorRol(ROL_VENDEDOR);
    }
    
    public static List<Empleado> obtenerMecanicos() {
        return obtenerPorRol(ROL_MECANICO);
    }
    
    public static void eliminarPorId(int id) {
        dao.eliminar(id);
    }
    
    @Override
    public String toString() {
        return getNombreCompletoConRol();
    }
}