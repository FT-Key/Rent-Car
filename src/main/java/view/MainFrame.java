package view;

import components.*;
import utils.UIStyles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Rent-Car Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        setJMenuBar(crearMenu());

        // Panel principal con texto centrado
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE); // fondo blanco
        panelCentral.setLayout(new GridBagLayout()); // permite centrar componentes

        JLabel titulo = new JLabel("Rent-Car");
        titulo.setFont(new Font("Arial", Font.BOLD, 72)); // letra grande y en negrita
        titulo.setForeground(new Color(30, 144, 255)); // color azul bonito

        panelCentral.add(titulo);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JMenuBar crearMenu() {
        StyledMenuBar menuBar = new StyledMenuBar();

        // Vehículos
        StyledMenu menuVehiculos = new StyledMenu("Vehículos");
        StyledMenuItem crudVehiculos = new StyledMenuItem("CRUD Vehículos");
        crudVehiculos.addActionListener(this::abrirVehiculoFrame);
        menuVehiculos.add(crudVehiculos);

        // Clientes
        StyledMenu menuClientes = new StyledMenu("Clientes");
        StyledMenuItem crudClientes = new StyledMenuItem("CRUD Clientes");
        crudClientes.addActionListener(this::abrirClienteFrame);
        menuClientes.add(crudClientes);

        // Alquileres
        StyledMenu menuAlquileres = new StyledMenu("Alquileres");
        StyledMenuItem registrarAlquiler = new StyledMenuItem("Registrar Alquiler");
        registrarAlquiler.addActionListener(this::abrirAlquilerFrame);
        menuAlquileres.add(registrarAlquiler);

        menuBar.add(menuVehiculos);
        menuBar.add(menuClientes);
        menuBar.add(menuAlquileres);

        return menuBar;
    }

    // ---------- Acciones para abrir frames ----------
    private void abrirVehiculoFrame(ActionEvent e) {
        VehiculoFrame frame = new VehiculoFrame();
        frame.setVisible(true);
    }

    private void abrirClienteFrame(ActionEvent e) {
        ClienteFrame frame = new ClienteFrame();
        frame.setVisible(true);
    }

    private void abrirAlquilerFrame(ActionEvent e) {
        AlquilerFrame frame = new AlquilerFrame();
        frame.setVisible(true);
    }

    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
