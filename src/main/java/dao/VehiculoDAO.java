package dao;

import conexion.ConexionSQL;
import model.Vehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public VehiculoDAO() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {

        String sql = """
            CREATE TABLE IF NOT EXISTS vehiculo (
                id INT AUTO_INCREMENT PRIMARY KEY,
                patente VARCHAR(30) NOT NULL UNIQUE,
                modelo VARCHAR(100) NOT NULL,
                km_incluido_por_dia DOUBLE NOT NULL,
                tarifa_por_dia DOUBLE NOT NULL,
                tarifa_extra_por_km DOUBLE NOT NULL,
                veces_alquilado INT DEFAULT 0
            )
        """;

        try (Connection con = ConexionSQL.getConnection();
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // AGREGAR
    public void agregar(Vehiculo v) {

        String sql = """
            INSERT INTO vehiculo 
            (patente, modelo, km_incluido_por_dia, tarifa_por_dia, tarifa_extra_por_km, veces_alquilado)
            VALUES (?, ?, ?, ?, ?, 0)
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getPatente());
            ps.setString(2, v.getModelo());
            ps.setDouble(3, v.getKmIncluidoPorDia());
            ps.setDouble(4, v.getTarifaPorDia());
            ps.setDouble(5, v.getTarifaExtraPorKm());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LISTAR
    public List<Vehiculo> listar() {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo";

        try (Connection con = ConexionSQL.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Vehiculo v = new Vehiculo(
                        rs.getInt("id"),
                        rs.getString("patente"),
                        rs.getString("modelo"),
                        rs.getDouble("km_incluido_por_dia"),
                        rs.getDouble("tarifa_por_dia"),
                        rs.getDouble("tarifa_extra_por_km")
                );

                v.setVecesAlquilado(rs.getInt("veces_alquilado"));

                lista.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public Vehiculo buscarPorId(int id) {

        String sql = "SELECT * FROM vehiculo WHERE id = ?";

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Vehiculo v = new Vehiculo(
                        rs.getInt("id"),
                        rs.getString("patente"),
                        rs.getString("modelo"),
                        rs.getDouble("km_incluido_por_dia"),
                        rs.getDouble("tarifa_por_dia"),
                        rs.getDouble("tarifa_extra_por_km")
                );

                v.setVecesAlquilado(rs.getInt("veces_alquilado"));

                return v;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ACTUALIZAR
    public void actualizar(Vehiculo v) {

        String sql = """
            UPDATE vehiculo SET
                patente = ?, 
                modelo = ?, 
                km_incluido_por_dia = ?, 
                tarifa_por_dia = ?, 
                tarifa_extra_por_km = ?
            WHERE id = ?
        """;

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getPatente());
            ps.setString(2, v.getModelo());
            ps.setDouble(3, v.getKmIncluidoPorDia());
            ps.setDouble(4, v.getTarifaPorDia());
            ps.setDouble(5, v.getTarifaExtraPorKm());
            ps.setInt(6, v.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(int id) {

        String sql = "DELETE FROM vehiculo WHERE id=?";

        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
