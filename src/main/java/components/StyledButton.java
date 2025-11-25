package components;

import utils.UIStyles;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;

public class StyledButton extends JButton {

    public StyledButton(String text) {
        super(text);
        setFont(UIStyles.FUENTE_BOTON);
        setBackground(UIStyles.COLOR_BOTON);
        setForeground(UIStyles.COLOR_BOTON_TEXTO);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        // Borde redondeado
        setBorder(new RoundedBorder(15)); // 15 px de radio

        // Efecto hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(UIStyles.COLOR_BOTON_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(UIStyles.COLOR_BOTON);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo redondeado
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        super.paintComponent(g2);
        g2.dispose();
    }

    // Clase interna para el borde redondeado
    private static class RoundedBorder implements Border {
        private final int radius;
        public RoundedBorder(int radius) { this.radius = radius; }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }
        @Override public boolean isBorderOpaque() { return false; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }
}
