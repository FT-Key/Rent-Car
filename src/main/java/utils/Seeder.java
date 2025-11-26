package utils;

import dao.ClienteDAO;
import dao.VehiculoDAO;
import dao.EmpleadoDAO;
import model.Cliente;
import model.Vehiculo;
import model.Empleado;

public class Seeder {

    public static void main(String[] args) {

        ClienteDAO clienteDAO = new ClienteDAO();
        VehiculoDAO vehiculoDAO = new VehiculoDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        // ----- Seed Clientes -----
        Cliente c1 = new Cliente(0, "Juan", "Pérez", "30111222", "1122334455",
                "juan@gmail.com", "Calle 123",
                "A12345", "B", "2026-05-20");

        Cliente c2 = new Cliente(0, "María", "López", "29333444", "1199988877",
                "maria@gmail.com", "Av. Siempre Viva 742",
                "B99887", "C", "2025-12-01");

        Cliente c3 = new Cliente(0, "Carlos", "Gómez", "31222444", "1156678899",
                "carlos@gmail.com", "Mitre 450",
                "C55443", "B", "2027-03-10");

        Cliente c4 = new Cliente(0, "Ana", "Torres", "28555111", "1177711122",
                "ana@gmail.com", "Belgrano 320",
                "D77889", "A", "2024-11-30");

        Cliente c5 = new Cliente(0, "Luis", "Fernández", "30111211", "1122233344",
                "luis@gmail.com", "Sarmiento 900",
                "A99221", "B", "2028-01-15");

        Cliente c6 = new Cliente(0, "Sofía", "Martínez", "31100999", "1144455566",
                "sofia@gmail.com", "Rivadavia 2000",
                "B21312", "C", "2026-07-08");

        clienteDAO.agregar(c1);
        clienteDAO.agregar(c2);
        clienteDAO.agregar(c3);
        clienteDAO.agregar(c4);
        clienteDAO.agregar(c5);
        clienteDAO.agregar(c6);

        System.out.println("Clientes seeders insertados.");

        // ----- Seed Vehículos -----
        Vehiculo v1 = new Vehiculo(0, "AAA111", "Toyota Corolla", 100, 2000, 50);
        Vehiculo v2 = new Vehiculo(0, "BBB222", "Honda Civic", 120, 2200, 60);
        Vehiculo v3 = new Vehiculo(0, "CCC333", "Ford Fiesta", 80, 1800, 40);
        Vehiculo v4 = new Vehiculo(0, "DDD444", "Chevrolet Onix", 90, 1900, 45);
        Vehiculo v5 = new Vehiculo(0, "EEE555", "Volkswagen Gol", 110, 2100, 55);
        Vehiculo v6 = new Vehiculo(0, "FFF666", "Renault Sandero", 95, 1950, 50);

        vehiculoDAO.agregar(v1);
        vehiculoDAO.agregar(v2);
        vehiculoDAO.agregar(v3);
        vehiculoDAO.agregar(v4);
        vehiculoDAO.agregar(v5);
        vehiculoDAO.agregar(v6);

        System.out.println("Vehículos seeders insertados.");

        // ----- Seed Empleados -----
        Empleado e1 = new Empleado(0, "Admin", "Principal", "00000001", "1122332211",
                "admin@sistema.com", "Oficina Central",
                "admin", "admin123", "admin");

        Empleado e2 = new Empleado(0, "Laura", "Gómez", "29911222", "1133445566",
                "laura@sistema.com", "Sucursal 1",
                "laura", "venta2024", "vendedor");

        Empleado e3 = new Empleado(0, "Pedro", "Martínez", "28833444", "1144556677",
                "pedro@sistema.com", "Taller",
                "pedro", "mecanico2024", "mecanico");

        empleadoDAO.agregar(e1);
        empleadoDAO.agregar(e2);
        empleadoDAO.agregar(e3);

        System.out.println("Empleados seeders insertados.");

        System.out.println("----- SEED COMPLETO -----");
    }
}
