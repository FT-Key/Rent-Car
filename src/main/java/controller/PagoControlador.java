package controller;

import model.Pago;
import java.util.List;

public class PagoControlador {

    public void pagar(int alquilerId, double monto, String metodo) throws Exception {
        Pago.registrarPago(alquilerId, monto, metodo);
    }

    public Pago crearPagoPendiente(int alquilerId, double monto, String metodo) throws Exception {
        Pago pago = Pago.crearPendiente(alquilerId, monto, metodo);
        pago.guardar();
        return pago;
    }

    public void marcarComoPagado(int idPago) throws Exception {
        List<Pago> pagos = Pago.obtenerTodos();
        for (Pago p : pagos) {
            if (p.getId() == idPago) {
                p.marcarComoPagado();
                break;
            }
        }
    }

    public void cancelarPago(int idPago) {
        List<Pago> pagos = Pago.obtenerTodos();
        for (Pago p : pagos) {
            if (p.getId() == idPago) {
                p.cancelar();
                break;
            }
        }
    }

    public void borrar(int id) {
        Pago.eliminarPorId(id);
    }

    public List<Pago> listar() {
        return Pago.obtenerTodos();
    }

    public List<Pago> listarPorAlquiler(int alquilerId) {
        return Pago.obtenerPorAlquiler(alquilerId);
    }

    public double obtenerTotalPagado(int alquilerId) {
        return Pago.obtenerTotalPagado(alquilerId);
    }

    // Verifica si un alquiler está completamente pagado
    public boolean alquilerPagado(int alquilerId, double montoTotal) {
        double totalPagado = Pago.obtenerTotalPagado(alquilerId);
        return totalPagado >= montoTotal;
    }
}
