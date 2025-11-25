package utils;

import dao.ClienteDAO;
import dao.VehiculoDAO;
import model.Cliente;
import model.Vehiculo;

public class Seeder {

    public static void main(String[] args) {
        ClienteDAO clienteDAO = new ClienteDAO();
        VehiculoDAO vehiculoDAO = new VehiculoDAO();

        // ----- Seed Clientes -----
        Cliente c1 = new Cliente(0, "Juan Pérez", "ABC123");
        Cliente c2 = new Cliente(0, "María López", "XYZ789");
        Cliente c3 = new Cliente(0, "Carlos Gómez", "LMN456");
        Cliente c4 = new Cliente(0, "Ana Torres", "DEF321");
        Cliente c5 = new Cliente(0, "Luis Fernández", "GHI654");
        Cliente c6 = new Cliente(0, "Sofía Martínez", "JKL987");

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
    }
}
