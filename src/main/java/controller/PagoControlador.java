package controller;

import service.PagoServicio;

public class PagoControlador {

    private PagoServicio service = new PagoServicio();

    public void pagar(int alquilerId, double monto, String metodo) throws Exception {
        service.registrarPago(alquilerId, monto, metodo);
    }

    public void borrar(int id) { service.eliminar(id); }
}
