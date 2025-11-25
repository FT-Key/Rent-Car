package dao;

import conexion.ConexionSQL;
import model.Alquiler;
import model.Vehiculo;
import model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlquilerDAO {

    // Constructor
    public AlquilerDAO() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = """
            CREATE TABLE IF NOT EXISTS alquiler (
                id INT AUTO_INCREMENT PRIMARY KEY,
                vehiculo_id INT NOT NULL,
                cliente_id INT NOT NULL,
                fecha_inicio DATE NOT NULL,
                fecha_fin DATE NOT NULL,
                km_inicio INT NOT NULL,
                km_fin INT NOT NULL,
                monto_total DOUBLE NOT NULL,
                devuelto BOOLEAN DEFAULT 0,
                FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id),
                FOREIGN KEY (cliente_id) REFERENCES cliente(id)
            )
        """;
        try (Connection con = ConexionSQL.getConnection();
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Registrar un alquiler
    public void registrar(Alquiler a) {
        a.calcularMontoTotal();
        String sql = "INSERT INTO alquiler (vehiculo_id, cliente_id, fecha_inicio, fecha_fin, km_inicio, km_fin, monto_total, devuelto) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getVehiculo().getId());
            ps.setInt(2, a.getCliente().getId());
            ps.setDate(3, Date.valueOf(a.getFechaInicio()));
            ps.setDate(4, Date.valueOf(a.getFechaFin()));
            ps.setInt(5, a.getKmInicio());
            ps.setInt(6, a.getKmFin());
            ps.setDouble(7, a.getMontoTotal());
            ps.executeUpdate();

            // Incrementar contador del vehículo
            String updateVehiculo = "UPDATE vehiculo SET veces_alquilado = veces_alquilado + 1 WHERE id = ?";
            try (PreparedStatement ps2 = con.prepareStatement(updateVehiculo)) {
                ps2.setInt(1, a.getVehiculo().getId());
                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Registrar devolución
    
    public void devolver(int idAlquiler, int kmFin) {
        String sql = "UPDATE alquiler SET km_fin = ?, devuelto = 1 WHERE id = ?";
        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, kmFin);
            ps.setInt(2, idAlquiler);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Listar todos los alquileres
    public List<Alquiler> listar() {
        return listarPorEstado(null); // null devuelve todos
    }

    // Listar por estado (true = devuelto, false = pendiente, null = todos)
    public List<Alquiler> listarPorEstado(Boolean devuelto) {
        List<Alquiler> lista = new ArrayList<>();
        String sql = """
            SELECT a.*, 
                   v.id AS vehiculo_id, v.patente, v.modelo, v.km_incluido_por_dia, v.tarifa_por_dia, v.tarifa_extra_por_km,
                   c.id AS cliente_id, c.nombre, c.licencia_conducir
            FROM alquiler a
            JOIN vehiculo v ON a.vehiculo_id = v.id
            JOIN cliente c ON a.cliente_id = c.id
        """;
        if (devuelto != null) sql += " WHERE a.devuelto = ?";

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (devuelto != null) ps.setBoolean(1, devuelto);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vehiculo v = new Vehiculo(
                        rs.getInt("vehiculo_id"),
                        rs.getString("patente"),
                        rs.getString("modelo"),
                        rs.getDouble("km_incluido_por_dia"),
                        rs.getDouble("tarifa_por_dia"),
                        rs.getDouble("tarifa_extra_por_km")
                );

                Cliente c = new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("nombre"),
                        rs.getString("licencia_conducir")
                );

                Alquiler a = new Alquiler(
                        rs.getInt("id"),
                        v,
                        c,
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate(),
                        rs.getInt("km_inicio"),
                        rs.getInt("km_fin"),
                        rs.getBoolean("devuelto")
                );
                a.setMontoTotal(rs.getDouble("monto_total"));
                lista.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Buscar por ID
    public Alquiler buscarPorId(int id) {
        List<Alquiler> lista = listarPorEstado(null);
        for (Alquiler a : lista) {
            if (a.getId() == id) return a;
        }
        return null;
    }
}
