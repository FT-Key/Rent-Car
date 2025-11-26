package components;

import utils.UIStyles;
import javax.swing.*;
import java.awt.*;

public class StyledHeader extends JPanel {
    
    public StyledHeader(String title, String subtitle) {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 144, 255));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Título principal
        JLabel labelTitulo = new JLabel(title);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        labelTitulo.setForeground(Color.WHITE);
        
        // Subtítulo
        JLabel labelSubtitulo = new JLabel(subtitle);
        labelSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labelSubtitulo.setForeground(new Color(220, 235, 255));
        
        // Panel de texto
        JPanel textoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textoPanel.setOpaque(false);
        textoPanel.add(labelTitulo);
        textoPanel.add(labelSubtitulo);
        
        add(textoPanel, BorderLayout.WEST);
    }
    
    // Constructor alternativo sin subtítulo
    public StyledHeader(String title) {
        this(title, "");
    }
}