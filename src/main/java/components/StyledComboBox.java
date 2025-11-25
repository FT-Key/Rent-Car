package components;

import utils.UIStyles;
import javax.swing.*;

public class StyledComboBox<T> extends JComboBox<T> {

    public StyledComboBox(T[] items) {
        super(items);
        setFont(UIStyles.FUENTE_TEXTO);
        setBackground(UIStyles.COLOR_FONDO_PANEL);
        setBorder(UIStyles.BORDE_INPUT);
    }
}
