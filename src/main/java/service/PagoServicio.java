package service;

import dao.PagoDAO;
import model.Pago;

import java.util.Date;
import java.util.List;

public class PagoServicio {

    private PagoDAO dao = new PagoDAO();

    public void registrarPago(int alquilerId, double monto, String metodo) throws Exception {

        if (monto <= 0)
            throw new Exception("El monto debe ser mayor a cero.");

        Pago pago = new Pago(
                0,
                alquilerId,
                monto,
                metodo,
                new Date(),
                "pagado"
        );

        dao.agregar(pago);
    }

    public List<Pago> listar() { return dao.listar(); }

    public void eliminar(int id) { dao.eliminar(id); }
}
