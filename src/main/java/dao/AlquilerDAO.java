package dao;

import conexion.ConexionSQL;
import model.Alquiler;
import model.Cliente;
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

    // CREA TABLA
    private void crearTablaSiNoExiste() {
        String sql = """
            CREATE TABLE IF NOT EXISTS alquiler (
                id INT AUTO_INCREMENT PRIMARY KEY,
                cliente_id INT NOT NULL,
                vehiculo_id INT NOT NULL,
                fecha_inicio DATE NOT NULL,
                fecha_fin DATE NOT NULL,
                precio_total DOUBLE NOT NULL,
                pago_id INT NULL,

                FOREIGN KEY (cliente_id)  REFERENCES cliente(id),
                FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id),
                FOREIGN KEY (pago_id)     REFERENCES pago(id)
            )
        """;

        try (Connection con = ConexionSQL.getConnection(); Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INSERTAR
    public void agregar(Alquiler a) {
        String sql = """
            INSERT INTO alquiler 
            (cliente_id, vehiculo_id, fecha_inicio, fecha_fin, precio_total, pago_id)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConexionSQL.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, a.getCliente().getId());
            ps.setInt(2, a.getVehiculo().getId());
            ps.setDate(3, Date.valueOf(a.getFechaInicio()));
            ps.setDate(4, Date.valueOf(a.getFechaFin()));
            ps.setDouble(5, a.getPrecioTotal());

            if (a.getPago() != null) {
                ps.setInt(6, a.getPago().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            int rowsAffected = ps.executeUpdate();
            System.out.println("Alquiler insertado, filas afectadas: " + rowsAffected);

            // Obtener el ID generado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                a.setId(generatedId);
                System.out.println("ID generado para el alquiler: " + generatedId);
            }

        } catch (SQLException e) {
            System.err.println("Error al agregar alquiler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ACTUALIZAR
    public void actualizar(Alquiler a) {
        String sql = """
            UPDATE alquiler SET 
                cliente_id=?, vehiculo_id=?,
                fecha_inicio=?, fecha_fin=?, precio_total=?, pago_id=?
            WHERE id=?
        """;

        try (Connection con = ConexionSQL.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getCliente().getId());
            ps.setInt(2, a.getVehiculo().getId());
            ps.setDate(3, Date.valueOf(a.getFechaInicio()));
            ps.setDate(4, Date.valueOf(a.getFechaFin()));
            ps.setDouble(5, a.getPrecioTotal());

            if (a.getPago() != null) {
                ps.setInt(6, a.getPago().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setInt(7, a.getId());

            int rows = ps.executeUpdate();
            System.out.println("Alquiler actualizado, filas afectadas: " + rows);

        } catch (SQLException e) {
            System.err.println("Error al actualizar alquiler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // LISTAR
    public List<Alquiler> listar() {
        List<Alquiler> lista = new ArrayList<>();

        String sql = """
        SELECT a.id AS alquiler_id,
               a.fecha_inicio, a.fecha_fin, a.precio_total, a.pago_id,
               a.cliente_id, a.vehiculo_id,

               c.nombre AS cli_nombre, c.apellido AS cli_apellido,
               c.dni AS cli_dni, c.telefono AS cli_tel, 
               c.email AS cli_email, c.direccion AS cli_dir,
               c.licencia_numero, c.licencia_categoria, c.licencia_vencimiento,

               v.patente, v.modelo, 
               v.km_incluido_por_dia, 
               v.tarifa_por_dia, 
               v.tarifa_extra_por_km, 
               v.veces_alquilado,

               p.id AS pago_id_real, p.monto, p.metodo, 
               p.fecha AS pago_fecha, p.estado

        FROM alquiler a
        INNER JOIN cliente c ON a.cliente_id = c.id
        INNER JOIN vehiculo v ON a.vehiculo_id = v.id
        LEFT JOIN pago p ON a.pago_id = p.id
    """;

        try (Connection con = ConexionSQL.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // CLIENTE
                Cliente cli = new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("cli_nombre"),
                        rs.getString("cli_apellido"),
                        rs.getString("cli_dni"),
                        rs.getString("cli_tel"),
                        rs.getString("cli_email"),
                        rs.getString("cli_dir"),
                        rs.getString("licencia_numero"), // ✅
                        rs.getString("licencia_categoria"), // ✅
                        rs.getString("licencia_vencimiento") // ✅
                );

                // VEHICULO
                Vehiculo v = new Vehiculo(
                        rs.getInt("vehiculo_id"),
                        rs.getString("patente"),
                        rs.getString("modelo"),
                        rs.getDouble("km_incluido_por_dia"),
                        rs.getDouble("tarifa_por_dia"),
                        rs.getDouble("tarifa_extra_por_km")
                );
                v.setVecesAlquilado(rs.getInt("veces_alquilado"));  // ✅

                // PAGO
                Pago pago = null;
                int pagoIdReal = rs.getInt("pago_id_real");
                if (!rs.wasNull()) {
                    pago = new Pago(
                            pagoIdReal,
                            rs.getInt("alquiler_id"),
                            rs.getDouble("monto"),
                            rs.getString("metodo"),
                            rs.getDate("pago_fecha"),
                            rs.getString("estado")
                    );
                }

                // ALQUILER
                Alquiler a = new Alquiler(
                        rs.getInt("alquiler_id"),
                        cli,
                        v,
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate(),
                        rs.getDouble("precio_total"),
                        pago
                );

                lista.add(a);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar alquileres: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public Alquiler buscarPorId(int id) {
        System.out.println("Buscando alquiler con ID: " + id);

        List<Alquiler> todos = listar();

        return todos.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // ELIMINAR
    public void eliminar(int id) {
        try (Connection con = ConexionSQL.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM alquiler WHERE id=?")) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println("Alquiler eliminado, filas afectadas: " + rows);

        } catch (SQLException e) {
            System.err.println("Error al eliminar alquiler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // VALIDAR DISPONIBILIDAD
    public boolean vehiculoEstaAlquilado(int vehiculoId, LocalDate inicio, LocalDate fin) {
        String sql = """
        SELECT COUNT(*) FROM alquiler
        WHERE vehiculo_id = ?
        AND pago_id IS NULL
        AND (
            (fecha_inicio <= ? AND fecha_fin >= ?)  
            OR
            (fecha_inicio <= ? AND fecha_fin >= ?)  
            OR
            (fecha_inicio >= ? AND fecha_fin <= ?)  
        )
        """;

        try (Connection conn = ConexionSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehiculoId);
            ps.setDate(2, java.sql.Date.valueOf(fin));
            ps.setDate(3, java.sql.Date.valueOf(inicio));
            ps.setDate(4, java.sql.Date.valueOf(fin));
            ps.setDate(5, java.sql.Date.valueOf(inicio));
            ps.setDate(6, java.sql.Date.valueOf(inicio));
            ps.setDate(7, java.sql.Date.valueOf(fin));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean alquilado = rs.getInt(1) > 0;
                System.out.println("Vehículo " + vehiculoId + " está alquilado: " + alquilado);
                return alquilado;
            }

        } catch (Exception e) {
            System.err.println("Error al verificar disponibilidad: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

}
