package components;

import utils.UIStyles;
import javax.swing.*;
import java.awt.*;

public class StyledMenuItem extends JMenuItem {

    public StyledMenuItem(String text) {
        super(text);
        setFont(UIStyles.FUENTE_TEXTO);
        setBackground(UIStyles.COLOR_FONDO_PANEL);
        setForeground(UIStyles.COLOR_TEXTO_PRINCIPAL);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
