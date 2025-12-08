package model;

import dao.PagoDAO;
import java.util.Date;
import java.util.List;

public class Pago {

    private int id;
    private int alquilerId;
    private double monto;
    private String metodo;
    private Date fecha;
    private String estado;

    private static PagoDAO dao = new PagoDAO();

    // Constantes para métodos de pago
    public static final String METODO_EFECTIVO = "efectivo";
    public static final String METODO_TARJETA = "tarjeta";
    public static final String METODO_TRANSFERENCIA = "transferencia";

    // Constantes para estados
    public static final String ESTADO_PAGADO = "pagado";
    public static final String ESTADO_PENDIENTE = "pendiente";
    public static final String ESTADO_CANCELADO = "cancelado";

    // Constructores
    public Pago() {
    }

    public Pago(int id, int alquilerId, double monto, String metodo, Date fecha, String estado) {
        this.id = id;
        this.alquilerId = alquilerId;
        this.monto = monto;
        this.metodo = metodo;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(int alquilerId) {
        this.alquilerId = alquilerId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // MÉTODOS DE INSTANCIA
    public void validar() throws Exception {
        if (alquilerId <= 0) {
            throw new Exception("El ID del alquiler es inválido");
        }
        if (monto <= 0) {
            throw new Exception("El monto debe ser mayor a cero");
        }
        if (metodo == null || metodo.isBlank()) {
            throw new Exception("El método de pago es obligatorio");
        }
        if (!esMetodoValido(metodo)) {
            throw new Exception("Método de pago inválido. Use: efectivo, tarjeta o transferencia");
        }
        if (fecha == null) {
            throw new Exception("La fecha es obligatoria");
        }
        if (estado == null || estado.isBlank()) {
            throw new Exception("El estado es obligatorio");
        }
        if (!esEstadoValido(estado)) {
            throw new Exception("Estado inválido. Use: pagado, pendiente o cancelado");
        }
    }

    public void guardar() throws Exception {
        validar();

        if (id == 0) {
            dao.agregar(this);
        }
    }

    public void eliminar() {
        if (id > 0) {
            dao.eliminar(id);
        }
    }

    public void marcarComoPagado() {
        this.estado = ESTADO_PAGADO;
        if (this.fecha == null) {
            this.fecha = new Date();
        }
    }

    public void marcarComoPendiente() {
        this.estado = ESTADO_PENDIENTE;
    }

    public void cancelar() {
        this.estado = ESTADO_CANCELADO;
    }

    public boolean estaPagado() {
        return ESTADO_PAGADO.equalsIgnoreCase(estado);
    }

    public boolean estaPendiente() {
        return ESTADO_PENDIENTE.equalsIgnoreCase(estado);
    }

    public boolean estaCancelado() {
        return ESTADO_CANCELADO.equalsIgnoreCase(estado);
    }

    private boolean esMetodoValido(String metodo) {
        return METODO_EFECTIVO.equalsIgnoreCase(metodo)
                || METODO_TARJETA.equalsIgnoreCase(metodo)
                || METODO_TRANSFERENCIA.equalsIgnoreCase(metodo);
    }

    private boolean esEstadoValido(String estado) {
        return ESTADO_PAGADO.equalsIgnoreCase(estado)
                || ESTADO_PENDIENTE.equalsIgnoreCase(estado)
                || ESTADO_CANCELADO.equalsIgnoreCase(estado);
    }

    public double calcularConDescuento(double porcentajeDescuento) {
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            return monto;
        }
        return monto * (1 - porcentajeDescuento / 100);
    }

    // MÉTODOS ESTÁTICOS
    public static Pago crear(int alquilerId, double monto, String metodo) {
        return new Pago(0, alquilerId, monto, metodo, new Date(), ESTADO_PAGADO);
    }

    public static Pago crearPendiente(int alquilerId, double monto, String metodo) {
        return new Pago(0, alquilerId, monto, metodo, new Date(), ESTADO_PENDIENTE);
    }

    public static Pago registrarPago(int alquilerId, double monto, String metodo) throws Exception {
        Pago pago = crear(alquilerId, monto, metodo);
        pago.guardar();
        return pago;
    }

    public static List<Pago> obtenerTodos() {
        return dao.listar();
    }

    public static List<Pago> obtenerPorAlquiler(int alquilerId) {
        return dao.listar().stream()
                .filter(p -> p.getAlquilerId() == alquilerId)
                .toList();
    }

    public static double obtenerTotalPagado(int alquilerId) {
        return obtenerPorAlquiler(alquilerId).stream()
                .filter(Pago::estaPagado)
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    public static void eliminarPorId(int id) {
        dao.eliminar(id);
    }

    @Override
    public String toString() {
        return String.format("Pago #%d - $%.2f (%s) - %s",
                id, monto, metodo, estado);
    }
}
