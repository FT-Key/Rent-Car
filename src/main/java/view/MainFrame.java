package view;

import components.StyledFooter;
import components.*;
import utils.UIStyles;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Rent-Car Management System");

        // Sin barra de título (quita X, minimizar, maximizar)
        setUndecorated(true);

        // Iniciar maximizado
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Tamaño por defecto si no está maximizado
        setSize(1200, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        setJMenuBar(crearMenu());

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 247, 250));

        panelPrincipal.add(crearHeader(), BorderLayout.NORTH);
        panelPrincipal.add(crearPanelCentral(), BorderLayout.CENTER);
        panelPrincipal.add(crearFooter(), BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private StyledHeader crearHeader() {
        return new StyledHeader(
                "Rent-Car",
                "Sistema integral de gestión de alquileres"
        );
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        StyledTitle tituloSeccion = new StyledTitle("Panel de Control", StyledTitle.TitleSize.LARGE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        panel.add(tituloSeccion, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 1.0;

        // -------- CARDS --------
        StyledCard cardVehiculos = new StyledCard("🚙", "Vehículos",
                "Gestiona tu flota de vehículos",
                new Color(52, 152, 219)
        );
        cardVehiculos.addCardActionListener(this::abrirVehiculoFrame);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(cardVehiculos, gbc);

        StyledCard cardClientes = new StyledCard("👥", "Clientes",
                "Administra tu cartera de clientes",
                new Color(46, 204, 113)
        );
        cardClientes.addCardActionListener(this::abrirClienteFrame);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(cardClientes, gbc);

        StyledCard cardEmpleados = new StyledCard("💼", "Empleados",
                "Gestiona el personal de tu empresa",
                new Color(155, 89, 182)
        );
        cardEmpleados.addCardActionListener(this::abrirEmpleadoFrame);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(cardEmpleados, gbc);

        StyledCard cardAlquileres = new StyledCard("📋", "Alquileres",
                "Registra y controla alquileres",
                new Color(230, 126, 34)
        );
        cardAlquileres.addCardActionListener(this::abrirAlquilerFrame);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(cardAlquileres, gbc);

        return panel;
    }

    private StyledFooter crearFooter() {
        return new StyledFooter();
    }

    private JMenuBar crearMenu() {
        StyledMenuBar menuBar = new StyledMenuBar();

        // Vehículos
        StyledMenu menuVehiculos = new StyledMenu("Vehículos");
        StyledMenuItem gestionarVehiculos = new StyledMenuItem("Gestionar Vehículos");
        gestionarVehiculos.addActionListener(this::abrirVehiculoFrame);
        menuVehiculos.add(gestionarVehiculos);

        // Clientes
        StyledMenu menuClientes = new StyledMenu("Clientes");
        StyledMenuItem gestionarClientes = new StyledMenuItem("Gestionar Clientes");
        gestionarClientes.addActionListener(this::abrirClienteFrame);
        menuClientes.add(gestionarClientes);

        // Empleados
        StyledMenu menuEmpleados = new StyledMenu("Empleados");
        StyledMenuItem gestionarEmpleados = new StyledMenuItem("Gestionar Empleados");
        gestionarEmpleados.addActionListener(this::abrirEmpleadoFrame);
        menuEmpleados.add(gestionarEmpleados);

        // Alquileres
        StyledMenu menuAlquileres = new StyledMenu("Alquileres");
        StyledMenuItem registrarAlquiler = new StyledMenuItem("Registrar Alquiler");
        registrarAlquiler.addActionListener(this::abrirAlquilerFrame);
        menuAlquileres.add(registrarAlquiler);

        // SISTEMA → SALIR
        StyledMenu menuSistema = new StyledMenu("Sistema");
        StyledMenuItem salir = new StyledMenuItem("Salir");
        salir.addActionListener(e -> System.exit(0));
        menuSistema.add(salir);

        // Agregar al menú
        menuBar.add(menuVehiculos);
        menuBar.add(menuClientes);
        menuBar.add(menuEmpleados);
        menuBar.add(menuAlquileres);
        menuBar.add(menuSistema);

        return menuBar;
    }

    //  ABRIR FRAMES "HIJOS" COMO DIALOG MODAL (bloquea MainFrame)
    private void abrirVehiculoFrame(ActionEvent e) {
        mostrarDialogo(new VehiculoFrame());
    }

    private void abrirClienteFrame(ActionEvent e) {
        mostrarDialogo(new ClienteFrame());
    }

    private void abrirEmpleadoFrame(ActionEvent e) {
        mostrarDialogo(new EmpleadoFrame());
    }

    private void abrirAlquilerFrame(ActionEvent e) {
        mostrarDialogo(new AlquilerFrame());
    }

    // ---------- Convierte los frames hijos en DIALOGS modales -----------
    private void mostrarDialogo(JFrame frameOriginal) {
        JDialog dialog = new JDialog(this, frameOriginal.getTitle(), true);
        dialog.setContentPane(frameOriginal.getContentPane());
        dialog.setSize(frameOriginal.getSize());
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
