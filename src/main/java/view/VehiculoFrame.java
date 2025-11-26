package view;

import components.*;
import controller.VehiculoControlador;
import model.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VehiculoFrame extends JFrame {

    private StyledTable tabla;
    private DefaultTableModel modelo;
    private VehiculoControlador controlador = new VehiculoControlador();

    // Campos para ingresar/editar vehículo
    private StyledTextField txtPatente;
    private StyledTextField txtModelo;
    private StyledTextField txtKmDia;
    private StyledTextField txtTarifaDia;
    private StyledTextField txtExtraKm;

    public VehiculoFrame() {
        setTitle("CRUD Vehículos");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla
        String[] columnas = {
            "ID", "Patente", "Modelo", "Km/día",
            "Tarifa/día", "Extra/km", "Veces alquilado"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new StyledTable(modelo);
        add(new StyledScrollPane(tabla), BorderLayout.CENTER);

        // Panel de formulario arriba
        StyledPanel panelForm = new StyledPanel(new GridLayout(2, 5, 10, 10));

        txtPatente = new StyledTextField();
        txtModelo = new StyledTextField();
        txtKmDia = new StyledTextField();
        txtTarifaDia = new StyledTextField();
        txtExtraKm = new StyledTextField();

        // Primera fila labels
        panelForm.add(new StyledLabel("Patente:"));
        panelForm.add(new StyledLabel("Modelo:"));
        panelForm.add(new StyledLabel("Km/día:"));
        panelForm.add(new StyledLabel("Tarifa/día:"));
        panelForm.add(new StyledLabel("Extra/km:"));

        // Segunda fila inputs
        panelForm.add(txtPatente);
        panelForm.add(txtModelo);
        panelForm.add(txtKmDia);
        panelForm.add(txtTarifaDia);
        panelForm.add(txtExtraKm);

        add(panelForm, BorderLayout.NORTH);

        // Panel de botones abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        StyledButton btnNuevo = new StyledButton("Nuevo");
        StyledButton btnGuardar = new StyledButton("Guardar");
        StyledButton btnActualizar = new StyledButton("Actualizar");
        StyledButton btnBorrar = new StyledButton("Borrar");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnBorrar);

        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnNuevo.addActionListener(this::accionNuevo);
        btnGuardar.addActionListener(this::accionGuardar);
        btnActualizar.addActionListener(this::accionActualizar);
        btnBorrar.addActionListener(this::accionBorrar);

        // Cargar datos
        cargarTabla();

        // Seleccionar fila
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionarFila());
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Vehiculo> lista = controlador.listar();
        for (Vehiculo v : lista) {
            modelo.addRow(new Object[]{
                v.getId(),
                v.getPatente(),
                v.getModelo(),
                v.getKmIncluidoPorDia(),
                v.getTarifaPorDia(),
                v.getTarifaExtraPorKm(),
                v.getVecesAlquilado()
            });
        }
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            txtPatente.setText(modelo.getValueAt(fila, 1).toString());
            txtModelo.setText(modelo.getValueAt(fila, 2).toString());
            txtKmDia.setText(modelo.getValueAt(fila, 3).toString());
            txtTarifaDia.setText(modelo.getValueAt(fila, 4).toString());
            txtExtraKm.setText(modelo.getValueAt(fila, 5).toString());
        }
    }

    private void accionNuevo(ActionEvent e) {
        txtPatente.setText("");
        txtModelo.setText("");
        txtKmDia.setText("");
        txtTarifaDia.setText("");
        txtExtraKm.setText("");
        tabla.clearSelection();
    }

    private void accionGuardar(ActionEvent e) {
        try {
            Vehiculo v = new Vehiculo();
            v.setPatente(txtPatente.getText().trim());
            v.setModelo(txtModelo.getText().trim());
            v.setKmIncluidoPorDia(Double.parseDouble(txtKmDia.getText().trim()));
            v.setTarifaPorDia(Double.parseDouble(txtTarifaDia.getText().trim()));
            v.setTarifaExtraPorKm(Double.parseDouble(txtExtraKm.getText().trim()));

            controlador.guardar(v);
            cargarTabla();
            accionNuevo(null);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Valores numéricos inválidos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un vehículo para actualizar",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Vehiculo v = new Vehiculo();
            v.setId((int) modelo.getValueAt(fila, 0));
            v.setPatente(txtPatente.getText().trim());
            v.setModelo(txtModelo.getText().trim());
            v.setKmIncluidoPorDia(Double.parseDouble(txtKmDia.getText().trim()));
            v.setTarifaPorDia(Double.parseDouble(txtTarifaDia.getText().trim()));
            v.setTarifaExtraPorKm(Double.parseDouble(txtExtraKm.getText().trim()));

            controlador.actualizar(v);
            cargarTabla();
            accionNuevo(null);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Valores numéricos inválidos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionBorrar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un vehículo para borrar",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        if (JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas borrar este vehículo?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            controlador.borrar(id);
            cargarTabla();
            accionNuevo(null);
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
        SwingUtilities.invokeLater(() -> new VehiculoFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
