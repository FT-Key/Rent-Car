package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQL {
    private static final String URL = "jdbc:mysql://localhost:3306/rent_car?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // si tenés contraseña en XAMPP, ponela aquí

    static {
        try {
            // Cargar driver (no siempre necesario pero lo pongo para compatibilidad)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver no encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
