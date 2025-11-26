package dao;

import conexion.ConexionSQL;
import model.Alquiler;
import model.Cliente;
import model.Empleado;
import model.Pago;
import model.Vehiculo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlquilerDAO {

    public AlquilerDAO() {
        crearTablaSiNoExiste();
    }

    // ======================================================
    // CREA TABLA
    // ======================================================
    private void crearTablaSiNoExiste() {
        String sql = """
            CREATE TABLE IF NOT EXISTS alquiler (
                id INT AUTO_INCREMENT PRIMARY KEY,
                cliente_id INT NOT NULL,
                vehiculo_id INT NOT NULL,
                empleado_id INT NOT NULL,
                fecha_inicio DATE NOT NULL,
                fecha_fin DATE NOT NULL,
                precio_total DOUBLE NOT NULL,
                pago_id INT NULL,

                FOREIGN KEY (cliente_id)  REFERENCES cliente(id),
                FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id),
                FOREIGN KEY (empleado_id) REFERENCES empleado(id),
                FOREIGN KEY (pago_id)     REFERENCES pago(id)
            )
        """;

        try (Connection con = ConexionSQL.getConnection(); 
            Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // INSERTAR (tu servicio lo llama como "agregar")
    // ======================================================
    public void agregar(Alquiler a) {
        String sql = """
            INSERT INTO alquiler 
            (cliente_id, vehiculo_id, empleado_id, fecha_inicio, fecha_fin, precio_total, pago_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getCliente().getId());
            ps.setInt(2, a.getVehiculo().getId());
            ps.setInt(3, a.getEmpleado().getId());
            ps.setDate(4, Date.valueOf(a.getFechaInicio()));
            ps.setDate(5, Date.valueOf(a.getFechaFin()));
            ps.setDouble(6, a.getPrecioTotal());

            if (a.getPago() != null) ps.setInt(7, a.getPago().getId());
            else ps.setNull(7, Types.INTEGER);

            ps.executeUpdate();

            // Incrementar contador del vehículo
            try (PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE vehiculo SET veces_alquilado = veces_alquilado + 1 WHERE id=?"
            )) {
                ps2.setInt(1, a.getVehiculo().getId());
                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // ACTUALIZAR
    // ======================================================
    public void actualizar(Alquiler a) {
        String sql = """
            UPDATE alquiler SET 
                cliente_id=?, vehiculo_id=?, empleado_id=?,
                fecha_inicio=?, fecha_fin=?, precio_total=?, pago_id=?
            WHERE id=?
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getCliente().getId());
            ps.setInt(2, a.getVehiculo().getId());
            ps.setInt(3, a.getEmpleado().getId());
            ps.setDate(4, Date.valueOf(a.getFechaInicio()));
            ps.setDate(5, Date.valueOf(a.getFechaFin()));
            ps.setDouble(6, a.getPrecioTotal());

            if (a.getPago() != null) ps.setInt(7, a.getPago().getId());
            else ps.setNull(7, Types.INTEGER);

            ps.setInt(8, a.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // LISTAR
    // ======================================================
    public List<Alquiler> listar() {
        List<Alquiler> lista = new ArrayList<>();

        String sql = """
            SELECT a.id AS alquiler_id,
                   a.fecha_inicio, a.fecha_fin, a.precio_total,
                   c.id AS cli_id, c.nombre AS cli_nombre, c.apellido AS cli_apellido,
                   c.dni AS cli_dni, c.telefono AS cli_tel, c.email AS cli_email, c.direccion AS cli_dir,
                   c.licenciaNumero, c.licenciaCategoria, c.licenciaVencimiento,
                   v.id AS veh_id, v.patente, v.modelo, v.kmIncluidoPorDia, v.tarifaPorDia, v.tarifaExtraPorKm,
                   e.id AS emp_id, e.nombre AS emp_nombre, e.apellido AS emp_apellido,
                   e.dni AS emp_dni, e.telefono AS emp_tel, e.email AS emp_email, e.direccion AS emp_dir,
                   e.usuario, e.password, e.rol,
                   p.id AS pago_id, p.monto, p.metodo, p.fecha, p.estado
            FROM alquiler a
            JOIN cliente c ON a.cliente_id = c.id
            JOIN vehiculo v ON a.vehiculo_id = v.id
            JOIN empleado e ON a.empleado_id = e.id
            LEFT JOIN pago p ON a.pago_id = p.id
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Cliente cli = new Cliente(
                        rs.getInt("cli_id"),
                        rs.getString("cli_nombre"),
                        rs.getString("cli_apellido"),
                        rs.getString("cli_dni"),
                        rs.getString("cli_tel"),
                        rs.getString("cli_email"),
                        rs.getString("cli_dir"),
                        rs.getString("licenciaNumero"),
                        rs.getString("licenciaCategoria"),
                        rs.getString("licenciaVencimiento")
                );

                Vehiculo v = new Vehiculo(
                        rs.getInt("veh_id"),
                        rs.getString("patente"),
                        rs.getString("modelo"),
                        rs.getDouble("kmIncluidoPorDia"),
                        rs.getDouble("tarifaPorDia"),
                        rs.getDouble("tarifaExtraPorKm")
                );

                Empleado emp = new Empleado(
                        rs.getInt("emp_id"),
                        rs.getString("emp_nombre"),
                        rs.getString("emp_apellido"),
                        rs.getString("emp_dni"),
                        rs.getString("emp_tel"),
                        rs.getString("emp_email"),
                        rs.getString("emp_dir"),
                        rs.getString("usuario"),
                        rs.getString("password"),
                        rs.getString("rol")
                );

                Pago pago = null;
                int pid = rs.getInt("pago_id");
                if (!rs.wasNull()) {
                    pago = new Pago(
                            pid,
                            rs.getInt("alquiler_id"),
                            rs.getDouble("monto"),
                            rs.getString("metodo"),
                            rs.getDate("fecha"),
                            rs.getString("estado")
                    );
                }

                Alquiler a = new Alquiler(
                        rs.getInt("alquiler_id"),
                        cli,
                        v,
                        emp,
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate(),
                        rs.getDouble("precio_total"),
                        pago
                );

                lista.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ======================================================
    // BUSCAR POR ID (consulta real, no listar() completo)
    // ======================================================
    public Alquiler buscarPorId(int id) {
        return listar().stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // ======================================================
    // ELIMINAR
    // ======================================================
    public void eliminar(int id) {
        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM alquiler WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // REQUERIDO POR TU SERVICIO: validar disponibilidad
    // ======================================================
    public boolean vehiculoEstaAlquilado(int vehiculoId, LocalDate inicio, LocalDate fin) {

        String sql = """
            SELECT COUNT(*) FROM alquiler
            WHERE vehiculo_id = ?
            AND (
               (fecha_inicio <= ? AND fecha_fin >= ?)  -- inicio dentro de otro alquiler
               OR
               (fecha_inicio <= ? AND fecha_fin >= ?)  -- fin dentro de otro alquiler
               OR
               (? <= fecha_inicio AND ? >= fecha_fin)  -- intervalo cubre otro alquiler
            )
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehiculoId);

            ps.setDate(2, Date.valueOf(inicio));
            ps.setDate(3, Date.valueOf(inicio));

            ps.setDate(4, Date.valueOf(fin));
            ps.setDate(5, Date.valueOf(fin));

            ps.setDate(6, Date.valueOf(inicio));
            ps.setDate(7, Date.valueOf(fin));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
