package view;

import components.*;
import dao.ClienteDAO;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ClienteFrame extends JFrame {

    private StyledTable tabla;
    private DefaultTableModel modelo;
    private ClienteDAO dao = new ClienteDAO();

    // Campos para agregar/editar
    private StyledTextField txtNombre;
    private StyledTextField txtLicencia;

    public ClienteFrame() {
        setTitle("CRUD Clientes");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla
        String[] columnas = {"ID", "Nombre", "Licencia"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new StyledTable(modelo);
        add(new StyledScrollPane(tabla), BorderLayout.CENTER);

        // Panel de formulario
        StyledPanel panelForm = new StyledPanel(new GridLayout(2, 2, 10, 10));
        txtNombre = new StyledTextField();
        txtLicencia = new StyledTextField();
        panelForm.add(new StyledLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new StyledLabel("Licencia:"));
        panelForm.add(txtLicencia);
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

        // Cargar datos iniciales
        cargarTabla();

        // Seleccionar fila para editar
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionarFila());
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Cliente> lista = dao.listar();
        for (Cliente c : lista) {
            modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getLicenciaConducir()});
        }
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            txtLicencia.setText(modelo.getValueAt(fila, 2).toString());
        }
    }

    private void accionNuevo(ActionEvent e) {
        txtNombre.setText("");
        txtLicencia.setText("");
        tabla.clearSelection();
    }

    private void accionGuardar(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        String licencia = txtLicencia.getText().trim();

        if (nombre.isEmpty() || licencia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente c = new Cliente();
        c.setNombre(nombre);
        c.setLicenciaConducir(licencia);

        dao.agregar(c);
        cargarTabla();
        accionNuevo(null);
    }

    private void accionActualizar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = txtNombre.getText().trim();
        String licencia = txtLicencia.getText().trim();

        Cliente c = new Cliente();
        c.setId(id);
        c.setNombre(nombre);
        c.setLicenciaConducir(licencia);

        dao.actualizar(c);
        cargarTabla();
        accionNuevo(null);
    }

    private void accionBorrar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para borrar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        int resp = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas borrar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
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

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ClienteFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
