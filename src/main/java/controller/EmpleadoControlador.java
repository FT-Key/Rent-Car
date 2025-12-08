package controller;

import model.Empleado;
import java.util.List;

public class EmpleadoControlador {
    
    /**
     * Guarda un nuevo empleado
     */
    public void guardarEmpleado(String nombre, String apellido, String dni, String telefono,
                                String email, String direccion,
                                String usuario, String password, String rol) throws Exception {
        Empleado empleado = Empleado.crear(nombre, apellido, dni, telefono, email, direccion,
                                          usuario, password, rol);
        empleado.guardar();
    }
    
    /**
     * Actualiza un empleado existente
     */
    public void actualizarEmpleado(int id, String nombre, String apellido, String dni, String telefono,
                                   String email, String direccion,
                                   String usuario, String password, String rol) throws Exception {
        Empleado empleado = new Empleado(id, nombre, apellido, dni, telefono, email, direccion,
                                        usuario, password, rol);
        empleado.guardar();
    }
    
    /**
     * Elimina un empleado por su ID
     */
    public void borrarEmpleado(int id) {
        Empleado.eliminarPorId(id);
    }
    
    /**
     * Obtiene todos los empleados
     */
    public List<Empleado> obtenerEmpleados() {
        return Empleado.obtenerTodos();
    }
    
    /**
     * Busca un empleado por su ID
     */
    public Empleado buscarEmpleado(int id) {
        return Empleado.buscarPorId(id);
    }
    
    /**
     * Busca un empleado por su usuario
     */
    public Empleado buscarPorUsuario(String usuario) {
        return Empleado.buscarPorUsuario(usuario);
    }
    
    /**
     * Autentica un empleado (login)
     * @param usuario Usuario del empleado
     * @param password Contraseña del empleado
     * @return Empleado autenticado o null si las credenciales son inválidas
     */
    public Empleado login(String usuario, String password) {
        return Empleado.autenticar(usuario, password);
    }
    
    /**
     * Cambia la contraseña de un empleado
     */
    public void cambiarPassword(int idEmpleado, String passwordActual, String passwordNueva) throws Exception {
        Empleado empleado = Empleado.buscarPorId(idEmpleado);
        if (empleado == null) {
            throw new Exception("Empleado no encontrado");
        }
        empleado.cambiarPassword(passwordActual, passwordNueva);
    }
    
    /**
     * Obtiene empleados por rol
     */
    public List<Empleado> obtenerPorRol(String rol) {
        return Empleado.obtenerPorRol(rol);
    }
    
    /**
     * Obtiene solo los administradores
     */
    public List<Empleado> obtenerAdministradores() {
        return Empleado.obtenerAdministradores();
    }
    
    /**
     * Obtiene solo los vendedores
     */
    public List<Empleado> obtenerVendedores() {
        return Empleado.obtenerVendedores();
    }
    
    /**
     * Obtiene solo los mecánicos
     */
    public List<Empleado> obtenerMecanicos() {
        return Empleado.obtenerMecanicos();
    }
    
    /**
     * Verifica si un usuario ya existe
     */
    public boolean usuarioExiste(String usuario) {
        return Empleado.usuarioExiste(usuario);
    }
    
    /**
     * Verifica los permisos de un empleado
     */
    public boolean verificarPermisosAdmin(int idEmpleado) {
        Empleado empleado = Empleado.buscarPorId(idEmpleado);
        return empleado != null && empleado.tienePermisosAdmin();
    }
    
    /**
     * Verifica si un empleado puede registrar alquileres
     */
    public boolean puedeRegistrarAlquileres(int idEmpleado) {
        Empleado empleado = Empleado.buscarPorId(idEmpleado);
        return empleado != null && empleado.puedeRegistrarAlquileres();
    }
    
    /**
     * Verifica si un empleado puede gestionar vehículos
     */
    public boolean puedeGestionarVehiculos(int idEmpleado) {
        Empleado empleado = Empleado.buscarPorId(idEmpleado);
        return empleado != null && empleado.puedeGestionarVehiculos();
    }
}