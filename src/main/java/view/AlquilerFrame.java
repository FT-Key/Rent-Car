package view;

import components.*;
import dao.AlquilerDAO;
import dao.VehiculoDAO;
import dao.ClienteDAO;
import model.Alquiler;
import model.Vehiculo;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class AlquilerFrame extends JFrame {

    private StyledTable tabla;
    private DefaultTableModel modelo;
    private AlquilerDAO alquilerDAO = new AlquilerDAO();
    private VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();

    private JComboBox<Cliente> comboCliente;
    private JComboBox<Vehiculo> comboVehiculo;

    public AlquilerFrame() {
        setTitle("Alquiler de Vehículos");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla de alquileres
        String[] columnas = {"ID", "Vehículo", "Cliente", "Inicio", "Fin", "Km Inicio", "Km Fin", "Monto", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new StyledTable(modelo);
        add(new StyledScrollPane(tabla), BorderLayout.CENTER);

        // Panel superior: selección de cliente y vehículo + botón registrar
        JPanel panelArriba = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        comboCliente = new JComboBox<>();
        comboVehiculo = new JComboBox<>();
        StyledButton btnRegistrar = new StyledButton("Registrar Alquiler");

        panelArriba.add(new JLabel("Cliente:"));
        panelArriba.add(comboCliente);
        panelArriba.add(new JLabel("Vehículo:"));
        panelArriba.add(comboVehiculo);
        panelArriba.add(btnRegistrar);

        add(panelArriba, BorderLayout.NORTH);

        // Panel inferior: devolución y filtro
        JPanel panelAbajo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        StyledButton btnDevolver = new StyledButton("Recibir Vehículo");
        JComboBox<String> comboFiltro = new JComboBox<>(new String[]{"Todos", "Pendientes", "Devueltos"});
        panelAbajo.add(btnDevolver);
        panelAbajo.add(new JLabel("Filtrar:"));
        panelAbajo.add(comboFiltro);
        add(panelAbajo, BorderLayout.SOUTH);

        // Cargar combos y tabla
        cargarCombos();
        cargarTabla();

        // Acciones
        btnRegistrar.addActionListener(this::accionRegistrar);
        btnDevolver.addActionListener(this::accionRecibir);
        comboFiltro.addActionListener(e -> filtrarTabla(comboFiltro.getSelectedIndex()));
    }

    private void cargarCombos() {
        comboCliente.removeAllItems();
        for (Cliente c : clienteDAO.listar()) {
            comboCliente.addItem(c);
        }

        comboVehiculo.removeAllItems();
        List<Alquiler> pendientes = alquilerDAO.listarPorEstado(false); // vehículos en alquiler pendientes
        for (Vehiculo v : vehiculoDAO.listar()) {
            boolean alquilado = pendientes.stream().anyMatch(a -> a.getVehiculo().getId() == v.getId());
            if (!alquilado) { // solo agregar vehículos disponibles
                comboVehiculo.addItem(v);
            }
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Alquiler a : alquilerDAO.listar()) {
            agregarFilaTabla(a);
        }
    }

    private void agregarFilaTabla(Alquiler a) {
        modelo.addRow(new Object[]{
            a.getId(),
            a.getVehiculo().getModelo(),
            a.getCliente().getNombre(),
            a.getFechaInicio(),
            a.getFechaFin(),
            a.getKmInicio(),
            a.getKmFin(),
            a.getMontoTotal(),
            a.isDevuelto() ? "Devuelto" : "Pendiente"
        });
    }

    private void filtrarTabla(int index) {
        Boolean devuelto = null;
        if (index == 1) {
            devuelto = false; // pendientes
        } else if (index == 2) {
            devuelto = true; // devueltos
        }
        modelo.setRowCount(0);
        for (Alquiler a : alquilerDAO.listarPorEstado(devuelto)) {
            agregarFilaTabla(a);
        }
    }

    private void accionRegistrar(ActionEvent e) {
        Cliente c = (Cliente) comboCliente.getSelectedItem();
        Vehiculo v = (Vehiculo) comboVehiculo.getSelectedItem();

        if (c == null || v == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar cliente y vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que el vehículo no esté actualmente alquilado y pendiente
        List<Alquiler> pendientes = alquilerDAO.listarPorEstado(false);
        boolean yaAlquilado = pendientes.stream().anyMatch(a -> a.getVehiculo().getId() == v.getId());
        if (yaAlquilado) {
            JOptionPane.showMessageDialog(this,
                    "Este vehículo ya está alquilado y pendiente de devolución.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Pedir fechas y km
        LocalDate inicio = LocalDate.now();
        LocalDate fin = inicio.plusDays(1); // por defecto 1 día
        int kmInicio = 0;
        int kmFin = 0;

        try {
            String strInicio = JOptionPane.showInputDialog(this, "Fecha de inicio (YYYY-MM-DD):", inicio.toString());
            if (strInicio != null) {
                inicio = LocalDate.parse(strInicio);
            }
            String strFin = JOptionPane.showInputDialog(this, "Fecha de fin (YYYY-MM-DD):", fin.toString());
            if (strFin != null) {
                fin = LocalDate.parse(strFin);
            }
            kmInicio = Integer.parseInt(JOptionPane.showInputDialog(this, "Kilómetros inicio:", "0"));
            kmFin = Integer.parseInt(JOptionPane.showInputDialog(this, "Kilómetros fin:", kmInicio + 100));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Alquiler a = new Alquiler();
        a.setCliente(c);
        a.setVehiculo(v);
        a.setFechaInicio(inicio);
        a.setFechaFin(fin);
        a.setKmInicio(kmInicio);
        a.setKmFin(kmFin);
        a.calcularMontoTotal();

        alquilerDAO.registrar(a);
        cargarTabla();
        cargarCombos(); // actualizar comboVehiculo para deshabilitar vehículos ya alquilados
    }

    private void accionRecibir(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un alquiler pendiente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        Alquiler a = alquilerDAO.buscarPorId(id);

        if (a == null || a.isDevuelto()) {
            JOptionPane.showMessageDialog(this, "Este alquiler ya fue devuelto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int kmFin = Integer.parseInt(JOptionPane.showInputDialog(this, "Kilómetros final:", a.getKmFin()));
            a.setKmFin(kmFin);
            a.calcularMontoTotal();
            alquilerDAO.devolver(a.getId(), kmFin);
            cargarTabla();
            cargarCombos(); // actualizar comboVehiculo para que vuelva a aparecer
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kilómetros inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
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
