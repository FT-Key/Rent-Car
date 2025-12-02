package dao;

import conexion.ConexionSQL;
import model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public EmpleadoDAO() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = """
            CREATE TABLE IF NOT EXISTS empleado (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                apellido VARCHAR(100) NOT NULL,
                dni VARCHAR(20) NOT NULL,
                telefono VARCHAR(50),
                email VARCHAR(100),
                direccion VARCHAR(200),
                usuario VARCHAR(100) NOT NULL,
                password VARCHAR(100) NOT NULL,
                rol VARCHAR(50) NOT NULL
            )
        """;

        try (Connection con = ConexionSQL.getConnection();
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INSERTAR
    public void agregar(Empleado e) {
        String sql = """
            INSERT INTO empleado
            (nombre, apellido, dni, telefono, email, direccion,
             usuario, password, rol)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getDni());
            ps.setString(4, e.getTelefono());
            ps.setString(5, e.getEmail());
            ps.setString(6, e.getDireccion());
            ps.setString(7, e.getUsuario());
            ps.setString(8, e.getPassword());
            ps.setString(9, e.getRol());

            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // LISTAR
    public List<Empleado> listar() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleado";

        try (Connection con = ConexionSQL.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Empleado e = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("usuario"),
                        rs.getString("password"),
                        rs.getString("rol")
                );
                lista.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    // ACTUALIZAR
    public void actualizar(Empleado e) {
        String sql = """
            UPDATE empleado SET
            nombre = ?, apellido = ?, dni = ?, telefono = ?, email = ?, direccion = ?,
            usuario = ?, password = ?, rol = ?
            WHERE id = ?
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getDni());
            ps.setString(4, e.getTelefono());
            ps.setString(5, e.getEmail());
            ps.setString(6, e.getDireccion());
            ps.setString(7, e.getUsuario());
            ps.setString(8, e.getPassword());
            ps.setString(9, e.getRol());
            ps.setInt(10, e.getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // DELETE
    public void eliminar(int id) {
        String sql = "DELETE FROM empleado WHERE id = ?";

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // BUSCAR POR ID
    public Empleado buscarPorId(int id) {
        String sql = "SELECT * FROM empleado WHERE id = ?";

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("usuario"),
                        rs.getString("password"),
                        rs.getString("rol")
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
