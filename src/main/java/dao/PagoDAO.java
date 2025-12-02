package dao;

import conexion.ConexionSQL;
import model.Pago;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {
    
    public PagoDAO() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = """
            CREATE TABLE IF NOT EXISTS pago (
                id INT AUTO_INCREMENT PRIMARY KEY,
                alquiler_id INT NOT NULL,
                monto DOUBLE NOT NULL,
                metodo VARCHAR(50) NOT NULL,
                fecha DATE NOT NULL,
                estado VARCHAR(20) NOT NULL,
                FOREIGN KEY (alquiler_id) REFERENCES alquiler(id)
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
    public void agregar(Pago p) {
        String sqlInsertPago = """
            INSERT INTO pago (alquiler_id, monto, metodo, fecha, estado)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        String sqlUpdateAlquiler = """
            UPDATE alquiler SET pago_id = ? WHERE id = ?
        """;

        Connection con = null;
        
        try {
            con = ConexionSQL.getConnection();
            con.setAutoCommit(false); // Iniciar transacción
            
            // 1. INSERTAR EL PAGO
            int pagoId = 0;
            try (PreparedStatement ps = con.prepareStatement(sqlInsertPago, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, p.getAlquilerId());
                ps.setDouble(2, p.getMonto());
                ps.setString(3, p.getMetodo());
                ps.setDate(4, new java.sql.Date(p.getFecha().getTime()));
                ps.setString(5, p.getEstado());
                
                ps.executeUpdate();
                
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    pagoId = rs.getInt(1);
                    p.setId(pagoId);
                    System.out.println("✓ Pago insertado con ID: " + pagoId);
                }
            }
            
            // 2. ACTUALIZAR EL ALQUILER CON EL PAGO_ID
            if (pagoId > 0) {
                try (PreparedStatement ps = con.prepareStatement(sqlUpdateAlquiler)) {
                    ps.setInt(1, pagoId);
                    ps.setInt(2, p.getAlquilerId());
                    
                    int rows = ps.executeUpdate();
                    System.out.println("✓ Alquiler ID " + p.getAlquilerId() + 
                                     " actualizado con pago_id=" + pagoId + 
                                     " (filas afectadas: " + rows + ")");
                }
            }
            
            con.commit(); // Confirmar transacción
            System.out.println("✓ Pago registrado y asociado exitosamente");
            
        } catch (SQLException e) {
            System.err.println("✗ Error al agregar pago: " + e.getMessage());
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                    System.err.println("✗ Transacción revertida");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Pago> listar() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago";
        
        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Pago p = new Pago(
                        rs.getInt("id"),
                        rs.getInt("alquiler_id"),
                        rs.getDouble("monto"),
                        rs.getString("metodo"),
                        rs.getDate("fecha"),
                        rs.getString("estado")
                );
                lista.add(p);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM pago WHERE id=?";
        
        try (Connection con = ConexionSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}