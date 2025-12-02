package components;

import utils.UIStyles;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class StyledCard extends JPanel {
    private final Color cardColor;
    private final Color borderNormal = new Color(220, 220, 220);
    private JButton actionButton;
    private JTextArea labelDesc;
    private JPanel botonWrapper;
    private static final int BORDER_RADIUS = 15;
    private static final int BREAKPOINT_WIDTH = 350; // Punto de quiebre para responsive

    public StyledCard(String icon, String title, String description, Color color) {
        this.cardColor = color;
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new RoundedBorder(borderNormal, 1, BORDER_RADIUS, 20));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(300, 350));
        setMinimumSize(new Dimension(200, 200));

        // Panel de contenido principal
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header con icono + texto alineados horizontal
        contenido.add(crearHeaderPanel(title, description, icon));
        contenido.add(Box.createVerticalStrut(15));

        // Botón
        botonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        botonWrapper.setOpaque(false);
        botonWrapper.add(crearBoton());
        contenido.add(botonWrapper);

        add(contenido, BorderLayout.CENTER);

        // Efectos hover
        agregarEfectosHover();

        // Listener para ajustar responsiveness
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarResponsive();
            }
        });
    }

    // ------------------------------
    // PANEL HEADER ÍCONO + TEXTO
    // ------------------------------
    private JPanel crearHeaderPanel(String title, String description, String icon) {
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout(15, 0));
        header.setOpaque(false);

        // Icono a la izquierda
        JPanel iconoPanel = crearIconoPanel(icon);
        header.add(iconoPanel, BorderLayout.WEST);

        // Panel de texto a la derecha
        JPanel textoPanel = new JPanel();
        textoPanel.setLayout(new BoxLayout(textoPanel, BoxLayout.Y_AXIS));
        textoPanel.setOpaque(false);

        JLabel labelTitulo = new JLabel(title);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 19));
        labelTitulo.setForeground(UIStyles.COLOR_TEXTO_PRINCIPAL);
        labelTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelDesc = new JTextArea(description);
        labelDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelDesc.setForeground(new Color(120, 120, 120));
        labelDesc.setLineWrap(true);
        labelDesc.setWrapStyleWord(true);
        labelDesc.setEditable(false);
        labelDesc.setOpaque(false);
        labelDesc.setFocusable(false);
        labelDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelDesc.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        textoPanel.add(labelTitulo);
        textoPanel.add(labelDesc);

        header.add(textoPanel, BorderLayout.CENTER);

        return header;
    }

    // ------------------------------
    // ÍCONO
    // ------------------------------
    private JPanel crearIconoPanel(String icon) {
        JLabel labelIcono = new JLabel(icon);
        labelIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        labelIcono.setHorizontalAlignment(SwingConstants.CENTER);
        labelIcono.setVerticalAlignment(SwingConstants.CENTER);

        JPanel iconoPanel = new JPanel(new GridBagLayout());
        iconoPanel.setBackground(new Color(cardColor.getRed(), cardColor.getGreen(),
                                           cardColor.getBlue(), 30));
        iconoPanel.setPreferredSize(new Dimension(85, 85));
        iconoPanel.setMinimumSize(new Dimension(70, 70));
        iconoPanel.setMaximumSize(new Dimension(85, 85));
        iconoPanel.setBorder(new RoundedBorder(cardColor, 2, 12, 5));
        iconoPanel.add(labelIcono);

        return iconoPanel;
    }

    // ------------------------------
    // BOTÓN
    // ------------------------------
    private JButton crearBoton() {
        actionButton = new JButton("Abrir");
        actionButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        actionButton.setForeground(Color.WHITE);
        actionButton.setBackground(cardColor);
        actionButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        actionButton.setFocusPainted(false);
        actionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover del botón
        Color colorHover = cardColor.darker();
        actionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                actionButton.setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                actionButton.setBackground(cardColor);
            }
        });

        return actionButton;
    }

    // ------------------------------
    // RESPONSIVE
    // ------------------------------
    private void ajustarResponsive() {
        int width = getWidth();

        // En pantallas pequeñas: ocultar descripción y botón
        if (width < BREAKPOINT_WIDTH) {
            labelDesc.setVisible(false);
            botonWrapper.setVisible(false);
        } else {
            // En pantallas grandes: mostrar todo
            labelDesc.setVisible(true);
            botonWrapper.setVisible(true);
        }

        revalidate();
        repaint();
    }

    // ------------------------------
    // EFECTOS DE HOVER EN LA CARD
    // ------------------------------
    private void agregarEfectosHover() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(new RoundedBorder(cardColor, 2, BORDER_RADIUS, 19));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(new RoundedBorder(borderNormal, 1, BORDER_RADIUS, 20));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                actionButton.doClick();
            }
        });
    }

    public void addCardActionListener(java.awt.event.ActionListener listener) {
        actionButton.addActionListener(listener);
    }

    // ------------------------------
    // BORDE REDONDEADO
    // ------------------------------
    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;
        private final int padding;

        RoundedBorder(Color color, int thickness, int radius, int padding) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
            this.padding = padding;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Double(x + thickness/2.0, y + thickness/2.0,
                    width - thickness, height - thickness, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(padding, padding, padding, padding);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = padding;
            return insets;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS));
        g2.dispose();
        super.paintComponent(g);
    }
}
