package view;

import components.*;
import dao.VehiculoDAO;
import model.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VehiculoFrame extends JFrame {

    private StyledTable tabla;
    private DefaultTableModel modelo;
    private VehiculoDAO dao = new VehiculoDAO();

    // Campos para ingresar/editar vehículo
    private StyledTextField txtPatente;
    private StyledTextField txtModelo;
    private StyledTextField txtKmDia;
    private StyledTextField txtTarifaDia;
    private StyledTextField txtExtraKm;

    public VehiculoFrame() {
        setTitle("CRUD Vehículos");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla
        String[] columnas = {"ID", "Patente", "Modelo", "Km/día", "Tarifa/día", "Extra/km", "Veces alquilado"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new StyledTable(modelo);
        add(new StyledScrollPane(tabla), BorderLayout.CENTER);

        // Panel de formulario arriba
        StyledPanel panelForm = new StyledPanel(new GridLayout(2, 5, 10, 10));
        txtPatente = new StyledTextField();
        txtModelo = new StyledTextField();
        txtKmDia = new StyledTextField();
        txtTarifaDia = new StyledTextField();
        txtExtraKm = new StyledTextField();

        panelForm.add(new StyledLabel("Patente:"));
        panelForm.add(new StyledLabel("Modelo:"));
        panelForm.add(new StyledLabel("Km/día:"));
        panelForm.add(new StyledLabel("Tarifa/día:"));
        panelForm.add(new StyledLabel("Extra/km:"));

        panelForm.add(txtPatente);
        panelForm.add(txtModelo);
        panelForm.add(txtKmDia);
        panelForm.add(txtTarifaDia);
        panelForm.add(txtExtraKm);

        add(panelForm, BorderLayout.NORTH);

        // Panel de botones
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

        // Seleccionar fila para editar
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionarFila());
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Vehiculo> lista = dao.listar();
        for (Vehiculo v : lista) {
            modelo.addRow(new Object[]{
                v.getId(), v.getPatente(), v.getModelo(),
                v.getKmIncluidoPorDia(), v.getTarifaPorDia(),
                v.getTarifaExtraPorKm(), v.getVecesAlquilado()
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
            String patente = txtPatente.getText().trim();
            String modeloTxt = txtModelo.getText().trim();
            double kmDia = Double.parseDouble(txtKmDia.getText().trim());
            double tarifaDia = Double.parseDouble(txtTarifaDia.getText().trim());
            double extraKm = Double.parseDouble(txtExtraKm.getText().trim());

            Vehiculo v = new Vehiculo();
            v.setPatente(patente);
            v.setModelo(modeloTxt);
            v.setKmIncluidoPorDia(kmDia);
            v.setTarifaPorDia(tarifaDia);
            v.setTarifaExtraPorKm(extraKm);

            dao.agregar(v);
            cargarTabla();
            accionNuevo(null);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa valores numéricos válidos para Km/día y tarifas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un vehículo para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = (int) modelo.getValueAt(fila, 0);
            String patente = txtPatente.getText().trim();
            String modeloTxt = txtModelo.getText().trim();
            double kmDia = Double.parseDouble(txtKmDia.getText().trim());
            double tarifaDia = Double.parseDouble(txtTarifaDia.getText().trim());
            double extraKm = Double.parseDouble(txtExtraKm.getText().trim());

            Vehiculo v = new Vehiculo();
            v.setId(id);
            v.setPatente(patente);
            v.setModelo(modeloTxt);
            v.setKmIncluidoPorDia(kmDia);
            v.setTarifaPorDia(tarifaDia);
            v.setTarifaExtraPorKm(extraKm);

            dao.actualizar(v);
            cargarTabla();
            accionNuevo(null);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa valores numéricos válidos para Km/día y tarifas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionBorrar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un vehículo para borrar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas borrar este vehículo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            dao.eliminar(id);
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
        SwingUtilities.invokeLater(() -> {
            new VehiculoFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
