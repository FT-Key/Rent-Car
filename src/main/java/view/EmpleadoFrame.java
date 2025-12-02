package view;

import components.*;
import controller.EmpleadoControlador;
import model.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EmpleadoFrame extends JFrame {

    private StyledTable tabla;
    private DefaultTableModel modelo;

    private EmpleadoControlador controlador = new EmpleadoControlador();

    // Campos del formulario
    private StyledTextField txtNombre;
    private StyledTextField txtApellido;
    private StyledTextField txtDni;
    private StyledTextField txtTelefono;
    private StyledTextField txtEmail;
    private StyledTextField txtDireccion;

    private StyledTextField txtUsuario;
    private StyledPasswordField txtPassword;
    private StyledTextField txtRol;

    public EmpleadoFrame() {

        setTitle("Gestión de Empleados - RentCar");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // TABLA
        String[] columnas = {
            "ID", "Nombre", "Apellido", "DNI",
            "Teléfono", "Email", "Dirección",
            "Usuario", "Rol"
        };

        modelo = new DefaultTableModel(columnas, 0);
        tabla = new StyledTable(modelo);

        add(new StyledScrollPane(tabla), BorderLayout.CENTER);

        // FORMULARIO
        StyledPanel panelForm = new StyledPanel(new GridLayout(9, 2, 10, 10));

        txtNombre = new StyledTextField();
        txtApellido = new StyledTextField();
        txtDni = new StyledTextField();
        txtTelefono = new StyledTextField();
        txtEmail = new StyledTextField();
        txtDireccion = new StyledTextField();

        txtUsuario = new StyledTextField();
        txtPassword = new StyledPasswordField();
        txtRol = new StyledTextField();

        panelForm.add(new StyledLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new StyledLabel("Apellido:"));
        panelForm.add(txtApellido);
        panelForm.add(new StyledLabel("DNI:"));
        panelForm.add(txtDni);
        panelForm.add(new StyledLabel("Teléfono:"));
        panelForm.add(txtTelefono);
        panelForm.add(new StyledLabel("Email:"));
        panelForm.add(txtEmail);
        panelForm.add(new StyledLabel("Dirección:"));
        panelForm.add(txtDireccion);

        panelForm.add(new StyledLabel("Usuario:"));
        panelForm.add(txtUsuario);
        panelForm.add(new StyledLabel("Contraseña:"));
        panelForm.add(txtPassword);
        panelForm.add(new StyledLabel("Rol:"));
        panelForm.add(txtRol);

        add(panelForm, BorderLayout.NORTH);

        // BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout());

        StyledButton btnNuevo = new StyledButton("Nuevo");
        StyledButton btnGuardar = new StyledButton("Guardar");
        StyledButton btnActualizar = new StyledButton("Actualizar");
        StyledButton btnBorrar = new StyledButton("Borrar");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnBorrar);

        add(panelBotones, BorderLayout.SOUTH);

        // ACCIONES
        btnNuevo.addActionListener(this::accionNuevo);
        btnGuardar.addActionListener(this::accionGuardar);
        btnActualizar.addActionListener(this::accionActualizar);
        btnBorrar.addActionListener(this::accionBorrar);

        tabla.getSelectionModel().addListSelectionListener(e -> seleccionarFila());

        cargarTabla();
    }

    // MÉTODOS
    private void cargarTabla() {
        modelo.setRowCount(0);

        for (Empleado e : controlador.obtenerEmpleados()) {
            modelo.addRow(new Object[]{
                e.getId(),
                e.getNombre(),
                e.getApellido(),
                e.getDni(),
                e.getTelefono(),
                e.getEmail(),
                e.getDireccion(),
                e.getUsuario(),
                e.getRol()
            });
        }
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            txtApellido.setText(modelo.getValueAt(fila, 2).toString());
            txtDni.setText(modelo.getValueAt(fila, 3).toString());
            txtTelefono.setText(modelo.getValueAt(fila, 4).toString());
            txtEmail.setText(modelo.getValueAt(fila, 5).toString());
            txtDireccion.setText(modelo.getValueAt(fila, 6).toString());

            txtUsuario.setText(modelo.getValueAt(fila, 7).toString());
            txtRol.setText(modelo.getValueAt(fila, 8).toString());

            txtPassword.setText(""); // No mostramos passwords desde la BD
        }
    }

    private void accionNuevo(ActionEvent e) {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");

        txtUsuario.setText("");
        txtPassword.setText("");
        txtRol.setText("");

        tabla.clearSelection();
    }

    private void accionGuardar(ActionEvent e) {
        try {
            controlador.guardarEmpleado(
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtDni.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText(),
                    txtDireccion.getText(),
                    txtUsuario.getText(),
                    new String(txtPassword.getPassword()),
                    txtRol.getText()
            );

            cargarTabla();
            accionNuevo(null);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        try {
            controlador.actualizarEmpleado(
                    id,
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtDni.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText(),
                    txtDireccion.getText(),
                    txtUsuario.getText(),
                    new String(txtPassword.getPassword()),
                    txtRol.getText()
            );

            cargarTabla();
            accionNuevo(null);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionBorrar(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        int resp = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este empleado?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            controlador.borrarEmpleado(id);
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
        SwingUtilities.invokeLater(() -> new EmpleadoFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
