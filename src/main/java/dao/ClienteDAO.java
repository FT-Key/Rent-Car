package dao;

import conexion.ConexionSQL;
import model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public ClienteDAO() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = """
            CREATE TABLE IF NOT EXISTS cliente (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                apellido VARCHAR(100) NOT NULL,
                dni VARCHAR(20) NOT NULL,
                telefono VARCHAR(50),
                email VARCHAR(100),
                direccion VARCHAR(200),
                licencia_numero VARCHAR(50) NOT NULL,
                licencia_categoria VARCHAR(20) NOT NULL,
                licencia_vencimiento VARCHAR(20) NOT NULL
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
    public void agregar(Cliente c) {
        String sql = """
            INSERT INTO cliente
            (nombre, apellido, dni, telefono, email, direccion,
             licencia_numero, licencia_categoria, licencia_vencimiento)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getDni());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getLicenciaNumero());
            ps.setString(8, c.getLicenciaCategoria());
            ps.setString(9, c.getLicenciaVencimiento());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LISTAR
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection con = ConexionSQL.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("licencia_numero"),
                        rs.getString("licencia_categoria"),
                        rs.getString("licencia_vencimiento")
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ACTUlIZAR
    public void actualizar(Cliente c) {
        String sql = """
            UPDATE cliente SET
            nombre = ?, apellido = ?, dni = ?, telefono = ?, email = ?, direccion = ?,
            licencia_numero = ?, licencia_categoria = ?, licencia_vencimiento = ?
            WHERE id = ?
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getDni());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getLicenciaNumero());
            ps.setString(8, c.getLicenciaCategoria());
            ps.setString(9, c.getLicenciaVencimiento());
            ps.setInt(10, c.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // BORRAR
    public void eliminar(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // BUSCAR POR ID
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("licencia_numero"),
                        rs.getString("licencia_categoria"),
                        rs.getString("licencia_vencimiento")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
