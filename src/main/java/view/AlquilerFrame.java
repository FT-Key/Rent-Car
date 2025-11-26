package view;

import components.*;
import controller.AlquilerControlador;
import controller.VehiculoControlador;
import controller.ClienteControlador;
import controller.PagoControlador;

import model.Alquiler;
import model.Cliente;
import model.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class AlquilerFrame extends JFrame {

    private StyledTable tabla;
    private DefaultTableModel modelo;

    private AlquilerControlador alquilerCtrl = new AlquilerControlador();
    private VehiculoControlador vehiculoCtrl = new VehiculoControlador();
    private ClienteControlador clienteCtrl = new ClienteControlador();
    private PagoControlador pagoCtrl = new PagoControlador();

    private JComboBox<Cliente> comboCliente;
    private JComboBox<Vehiculo> comboVehiculo;

    public AlquilerFrame() {

        setTitle("Gestión de Alquileres");
        setSize(1100, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TABLA
        modelo = new DefaultTableModel(
                new String[]{"ID", "Vehículo", "Cliente", "Inicio", "Fin", "Precio Base", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new StyledTable(modelo);
        add(new StyledScrollPane(tabla), BorderLayout.CENTER);

        // PANEL SUPERIOR (REGISTRO)
        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        comboCliente = new JComboBox<>();
        comboVehiculo = new JComboBox<>();

        StyledButton btnRegistrar = new StyledButton("Registrar Alquiler");

        arriba.add(new JLabel("Cliente:"));
        arriba.add(comboCliente);
        arriba.add(new JLabel("Vehículo:"));
        arriba.add(comboVehiculo);
        arriba.add(btnRegistrar);

        add(arriba, BorderLayout.NORTH);

        // PANEL INFERIOR (ACCIONES)
        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        StyledButton btnDevolver = new StyledButton("Registrar Devolución");
        JComboBox<String> filtro = new JComboBox<>(new String[]{"Todos", "Pendientes", "Devueltos"});

        abajo.add(btnDevolver);
        abajo.add(new JLabel("Filtrar:"));
        abajo.add(filtro);

        add(abajo, BorderLayout.SOUTH);

        // CARGA INICIAL
        cargarClientes();
        cargarVehiculosDisponibles();
        cargarTabla();

        // ACCIONES
        btnRegistrar.addActionListener(this::registrarAlquiler);
        btnDevolver.addActionListener(this::registrarDevolucion);
        filtro.addActionListener(e -> filtrarTabla(filtro.getSelectedIndex()));
    }

    // =================================================================================
    // MÉTODOS DE CARGA
    // =================================================================================
    private void cargarClientes() {
        comboCliente.removeAllItems();
        clienteCtrl.obtenerClientes().forEach(comboCliente::addItem);
    }

    private void cargarVehiculosDisponibles() {

        comboVehiculo.removeAllItems();

        List<Alquiler> pendientes = alquilerCtrl.listar().stream()
                .filter(a -> a.getPago() == null)
                .toList();

        for (Vehiculo v : vehiculoCtrl.listar()) {
            boolean alquilado = pendientes.stream()
                    .anyMatch(a -> a.getVehiculo().getId() == v.getId());
            if (!alquilado) {
                comboVehiculo.addItem(v);
            }
        }
    }

    // =================================================================================
    // TABLA
    // =================================================================================
    private void cargarTabla() {
        modelo.setRowCount(0);
        alquilerCtrl.listar().forEach(this::agregarFila);
    }

    private void agregarFila(Alquiler a) {

        String estado = (a.getPago() != null) ? "Devuelto" : "Pendiente";

        modelo.addRow(new Object[]{
            a.getId(),
            a.getVehiculo().getModelo(),
            a.getCliente().getNombre(),
            a.getFechaInicio(),
            a.getFechaFin(),
            a.getPrecioTotal(),
            estado
        });
    }

    private void filtrarTabla(int tipo) {

        modelo.setRowCount(0);

        alquilerCtrl.listar().stream()
                .filter(a -> {
                    if (tipo == 1) {
                        return a.getPago() == null;      // Pendientes
                    }
                    if (tipo == 2) {
                        return a.getPago() != null;      // Devueltos
                    }
                    return true;                                     // Todos
                })
                .forEach(this::agregarFila);
    }

    // =================================================================================
    // REGISTRAR ALQUILER
    // =================================================================================
    private void registrarAlquiler(ActionEvent e) {

        Cliente c = (Cliente) comboCliente.getSelectedItem();
        Vehiculo v = (Vehiculo) comboVehiculo.getSelectedItem();

        if (c == null || v == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar cliente y vehículo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String iniStr = JOptionPane.showInputDialog(this, "Fecha inicio (YYYY-MM-DD):", LocalDate.now());
            String finStr = JOptionPane.showInputDialog(this, "Fecha fin (YYYY-MM-DD):", LocalDate.now().plusDays(1));

            LocalDate ini = LocalDate.parse(iniStr);
            LocalDate fin = LocalDate.parse(finStr);

            alquilerCtrl.crear(c, v, null, ini, fin);

            // COBRO INICIAL
            double monto = v.getTarifaPorDia();
            pagoCtrl.pagar(alquilerCtrl.listar().getLast().getId(), monto, "Efectivo");

            cargarTabla();
            cargarVehiculosDisponibles();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =================================================================================
    // REGISTRAR DEVOLUCIÓN
    // =================================================================================
    private void registrarDevolucion(ActionEvent e) {

        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un alquiler pendiente.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        // si ya está devuelto → no permitir
        Alquiler a = alquilerCtrl.listar().stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElse(null);

        if (a == null || a.getPago() != null) {
            JOptionPane.showMessageDialog(this,
                    "Este alquiler ya fue devuelto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String kmFinStr = JOptionPane.showInputDialog(this, "Kilómetros finales:");
            double kmFin = Double.parseDouble(kmFinStr);

            double extra = alquilerCtrl.devolverVehiculo(id, kmFin);

            // Si hay que cobrar extra
            if (extra > 0) {
                pagoCtrl.pagar(id, extra, "Efectivo");
                JOptionPane.showMessageDialog(this,
                        "Se cobraron km extra: $" + extra,
                        "Cobro realizado", JOptionPane.INFORMATION_MESSAGE);
            }

            cargarTabla();
            cargarVehiculosDisponibles();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        SwingUtilities.invokeLater(() -> new AlquilerFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
