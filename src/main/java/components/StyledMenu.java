package components;

import utils.UIStyles;
import javax.swing.*;

public class StyledMenu extends JMenu {

    public StyledMenu(String text) {
        super(text);
        setForeground(UIStyles.COLOR_BOTON_TEXTO);
        setFont(UIStyles.FUENTE_LABEL);
    }
}
