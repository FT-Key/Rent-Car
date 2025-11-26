package components;

import javax.swing.*;
import java.awt.*;

public class StyledFooter extends JPanel {
    
    public StyledFooter(String text) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(new Color(52, 73, 94));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel copyright = new JLabel(text);
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyright.setForeground(new Color(189, 195, 199));
        
        add(copyright);
    }
    
    // Constructor con texto por defecto
    public StyledFooter() {
        this("© 2025 Rent-Car System | Todos los derechos reservados");
    }
}