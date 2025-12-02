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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

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
                new String[]{"ID", "Vehículo", "Cliente", "Inicio", "Fin", "Precio Total", "Estado"}, 0
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
        StyledButton btnRefresh = new StyledButton("Actualizar");
        JComboBox<String> filtro = new JComboBox<>(new String[]{"Todos", "Pendientes", "Devueltos"});

        abajo.add(btnDevolver);
        abajo.add(btnRefresh);
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
        btnRefresh.addActionListener(e -> {
            cargarTabla();
            cargarVehiculosDisponibles();
        });
        filtro.addActionListener(e -> filtrarTabla(filtro.getSelectedIndex()));
    }

    // MÉTODOS DE CARGA
    private void cargarClientes() {
        comboCliente.removeAllItems();
        List<Cliente> clientes = clienteCtrl.obtenerClientes();
        if (clientes == null || clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay clientes registrados. Registre clientes primero.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        clientes.forEach(comboCliente::addItem);
    }

    private void cargarVehiculosDisponibles() {
        comboVehiculo.removeAllItems();

        // Usar el controlador para obtener vehículos disponibles
        List<Vehiculo> disponibles = alquilerCtrl.obtenerVehiculosDisponibles();

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay vehículos disponibles.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        disponibles.forEach(comboVehiculo::addItem);
    }

    // TABLA
    private void cargarTabla() {
        modelo.setRowCount(0);

        try {
            List<Alquiler> alquileres = alquilerCtrl.listar();

            System.out.println("==== DEBUG CARGAR TABLA ====");
            System.out.println("Alquileres obtenidos: " + (alquileres == null ? "NULL" : alquileres.size()));

            if (alquileres == null || alquileres.isEmpty()) {
                System.out.println("No hay alquileres para mostrar");
                return;
            }

            alquileres.forEach(a -> {
                try {
                    System.out.println("Alquiler ID: " + a.getId()
                            + " - Cliente: " + (a.getCliente() != null ? a.getCliente().getNombre() : "NULL")
                            + " - Vehículo: " + (a.getVehiculo() != null ? a.getVehiculo().getModelo() : "NULL"));
                    agregarFila(a);
                } catch (Exception e) {
                    System.err.println("Error al agregar fila: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println("Error al cargar tabla: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar alquileres: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarFila(Alquiler a) {
        String estado = (a.getPago() != null) ? "Devuelto" : "Pendiente";

        modelo.addRow(new Object[]{
            a.getId(),
            a.getVehiculo().getModelo(),
            a.getCliente().getNombre() + " " + a.getCliente().getApellido(),
            a.getFechaInicio(),
            a.getFechaFin(),
            String.format("$%.2f", a.getPrecioTotal()),
            estado
        });
    }

    private void filtrarTabla(int tipo) {
        modelo.setRowCount(0);

        List<Alquiler> alquileres;

        // Usar métodos del controlador según el filtro
        if (tipo == 1) {
            alquileres = alquilerCtrl.listarPendientes();
        } else if (tipo == 2) {
            alquileres = alquilerCtrl.listarDevueltos();
        } else {
            alquileres = alquilerCtrl.listar();
        }

        alquileres.forEach(this::agregarFila);
    }

    // REGISTRAR ALQUILER
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
            // Fecha de inicio es SIEMPRE HOY
            LocalDate ini = LocalDate.now();

            // Solicitar solo la fecha de fin
            LocalDate fin = null;
            boolean fechaValida = false;

            while (!fechaValida) {
                String finStr = JOptionPane.showInputDialog(this,
                        String.format("Fecha de inicio: %s (HOY)\n\n"
                                + "Ingrese fecha de devolución (YYYY-MM-DD):\n"
                                + "(Debe ser posterior a hoy)",
                                ini.toString()),
                        ini.plusDays(3).toString());

                if (finStr == null || finStr.trim().isEmpty()) {
                    return; // Usuario canceló
                }

                try {
                    fin = LocalDate.parse(finStr.trim());

                    // Validar que sea posterior a hoy
                    if (fin.isBefore(ini) || fin.isEqual(ini)) {
                        JOptionPane.showMessageDialog(this,
                                "La fecha de devolución debe ser posterior a hoy.\n"
                                + "Hoy es: " + ini,
                                "Fecha Inválida",
                                JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    fechaValida = true;

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Formato de fecha inválido.\n"
                            + "Use el formato: YYYY-MM-DD\n"
                            + "Ejemplo: " + LocalDate.now().plusDays(3),
                            "Error de Formato",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            // Calcular precio usando el controlador
            double precioTotal = alquilerCtrl.calcularPrecio(v, ini, fin);
            long dias = ChronoUnit.DAYS.between(ini, fin) + 1;

            // Confirmar con el usuario
            int confirm = JOptionPane.showConfirmDialog(this,
                    String.format("RESUMEN DEL ALQUILER\n\n"
                            + "Cliente: %s %s\n"
                            + "Vehículo: %s (%s)\n"
                            + "─────────────────────────────\n"
                            + "Fecha de inicio: %s (HOY)\n"
                            + "Fecha de devolución: %s\n"
                            + "Duración: %d día%s\n"
                            + "─────────────────────────────\n"
                            + "Tarifa por día: $%.2f\n"
                            + "PRECIO TOTAL: $%.2f\n\n"
                            + "¿Confirmar registro del alquiler?",
                            c.getNombre(), c.getApellido(),
                            v.getModelo(), v.getPatente(),
                            ini, fin, dias, (dias > 1 ? "s" : ""),
                            v.getTarifaPorDia(), precioTotal),
                    "Confirmar Alquiler",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // CREAR ALQUILER
            alquilerCtrl.crear(c, v, ini, fin);

            JOptionPane.showMessageDialog(this,
                    String.format("✓ ALQUILER REGISTRADO EXITOSAMENTE\n\n"
                            + "Cliente: %s %s\n"
                            + "Vehículo: %s\n"
                            + "Desde: %s\n"
                            + "Hasta: %s\n"
                            + "Duración: %d día%s\n"
                            + "Precio base: $%.2f\n\n"
                            + "Estado: PENDIENTE\n\n"
                            + "El cobro se realizará al momento\n"
                            + "de la devolución del vehículo.",
                            c.getNombre(), c.getApellido(),
                            v.getModelo(),
                            ini, fin, dias, (dias > 1 ? "s" : ""),
                            precioTotal),
                    "Alquiler Registrado",
                    JOptionPane.INFORMATION_MESSAGE);

            // Actualizar pantalla
            cargarTabla();
            cargarVehiculosDisponibles();

        } catch (Exception ex) {
            mostrarError("Error al registrar alquiler", ex);
        }
    }

    // REGISTRAR DEVOLUCIÓN (CORREGIDO)
    private void registrarDevolucion(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un alquiler de la tabla.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        try {
            // Solicitar kilómetros
            String kmFinStr = JOptionPane.showInputDialog(this,
                    "Ingrese kilómetros recorridos:");

            if (kmFinStr == null || kmFinStr.trim().isEmpty()) {
                return;
            }

            double kmRecorridos = Double.parseDouble(kmFinStr.trim());

            if (kmRecorridos < 0) {
                JOptionPane.showMessageDialog(this,
                        "Los kilómetros no pueden ser negativos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular resumen usando el controlador
            Map<String, Double> resumen = alquilerCtrl.calcularResumenDevolucion(id, kmRecorridos);

            double precioBase = resumen.get("precioBase");
            double kmIncluidos = resumen.get("kmIncluidos");
            double kmExtra = resumen.get("kmExtra");
            double costoExtra = resumen.get("costoExtra");
            double montoTotal = resumen.get("total");
            long dias = resumen.get("dias").longValue();

            // COBRAR EL TOTAL
            String[] opciones = {"Efectivo", "Tarjeta", "Transferencia"};
            String mensaje = String.format(
                    "RESUMEN DE DEVOLUCIÓN\n\n"
                    + "Días de alquiler: %d\n"
                    + "Precio base: $%.2f\n"
                    + "Kilómetros incluidos: %.0f km\n"
                    + "Kilómetros recorridos: %.0f km\n"
                    + "Kilómetros extra: %.0f km\n"
                    + "Cargo por km extra: $%.2f\n"
                    + "─────────────────────\n"
                    + "TOTAL A COBRAR: $%.2f\n\n"
                    + "Seleccione método de pago:",
                    dias, precioBase, kmIncluidos, kmRecorridos,
                    kmExtra, costoExtra, montoTotal);

            String metodoPago = (String) JOptionPane.showInputDialog(this,
                    mensaje,
                    "Cobro de Devolución",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            if (metodoPago == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un método de pago para completar la devolución.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Procesar la devolución
            alquilerCtrl.devolverVehiculo(id, kmRecorridos);

            // Registrar el pago con el monto total
            pagoCtrl.pagar(id, montoTotal, metodoPago);

            if (costoExtra > 0) {
                JOptionPane.showMessageDialog(this,
                        String.format("Devolución completada exitosamente.\n\n"
                                + "Precio base: $%.2f\n"
                                + "Cargo extra: $%.2f\n"
                                + "Total cobrado: $%.2f",
                                precioBase, costoExtra, montoTotal),
                        "Devolución Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        String.format("Devolución completada sin cargos adicionales.\n\n"
                                + "Total cobrado: $%.2f", montoTotal),
                        "Devolución Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Actualizar pantalla
            cargarTabla();
            cargarVehiculosDisponibles();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar un número válido de kilómetros.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            mostrarError("Error al registrar devolución", ex);
        }
    }

    // UTILIDADES
    private void mostrarError(String mensaje, Exception ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Error desconocido";
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String stack = sw.toString();

        JTextArea textArea = new JTextArea(stack);
        textArea.setEditable(false);
        textArea.setRows(15);
        textArea.setColumns(60);
        JScrollPane scroll = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, new Object[]{
            new JLabel("<html><b>" + mensaje + ":</b> " + msg + "</html>"),
            scroll
        }, "Error", JOptionPane.ERROR_MESSAGE);
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
