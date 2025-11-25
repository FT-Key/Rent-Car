package dao;

import conexion.ConexionSQL;
import model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Constructor
    public ClienteDAO() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = """
            CREATE TABLE IF NOT EXISTS cliente (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                licencia_conducir VARCHAR(50) NOT NULL
            )
        """;
        try (Connection con = ConexionSQL.getConnection();
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----- Agregar Cliente -----
    public void agregar(Cliente c) {
        String sql = "INSERT INTO cliente (nombre, licencia_conducir) VALUES (?, ?)";
        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getLicenciaConducir());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----- Listar todos los clientes -----
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
                        rs.getString("licencia_conducir")
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ----- Actualizar Cliente -----
    public void actualizar(Cliente c) {
        String sql = "UPDATE cliente SET nombre = ?, licencia_conducir = ? WHERE id = ?";
        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getLicenciaConducir());
            ps.setInt(3, c.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----- Eliminar Cliente -----
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

    // ----- Buscar Cliente por ID -----
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
                        rs.getString("licencia_conducir")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
