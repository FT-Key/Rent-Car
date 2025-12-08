package model;

import dao.ClienteDAO;
import java.util.List;

public class Cliente extends Persona {

    private String licenciaNumero;
    private String licenciaCategoria;
    private String licenciaVencimiento;

    private static ClienteDAO dao = new ClienteDAO();

    // Constructores
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
    public String getLicenciaNumero() {
        return licenciaNumero;
    }

    public void setLicenciaNumero(String licenciaNumero) {
        this.licenciaNumero = licenciaNumero;
    }

    public String getLicenciaCategoria() {
        return licenciaCategoria;
    }

    public void setLicenciaCategoria(String licenciaCategoria) {
        this.licenciaCategoria = licenciaCategoria;
    }

    public String getLicenciaVencimiento() {
        return licenciaVencimiento;
    }

    public void setLicenciaVencimiento(String licenciaVencimiento) {
        this.licenciaVencimiento = licenciaVencimiento;
    }

    // MÉTODOS DE INSTANCIA
    public void validar() throws Exception {
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
        if (licenciaNumero == null || licenciaNumero.isBlank()) {
            throw new Exception("El número de licencia es obligatorio");
        }
        if (licenciaCategoria == null || licenciaCategoria.isBlank()) {
            throw new Exception("La categoría de licencia es obligatoria");
        }
        if (licenciaVencimiento == null || licenciaVencimiento.isBlank()) {
            throw new Exception("El vencimiento de licencia es obligatorio");
        }
    }

    public void guardar() throws Exception {
        validar();

        if (getId() == 0) {
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

    // MÉTODOS ESTÁTICOS
    public static Cliente crear(String nombre, String apellido, String dni, String telefono,
            String email, String direccion, String licenciaNumero,
            String licenciaCategoria, String licenciaVencimiento) {
        return new Cliente(0, nombre, apellido, dni, telefono, email, direccion,
                licenciaNumero, licenciaCategoria, licenciaVencimiento);
    }

    public static List<Cliente> obtenerTodos() {
        return dao.listar();
    }

    public static Cliente buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public static void eliminarPorId(int id) {
        dao.eliminar(id);
    }
}
