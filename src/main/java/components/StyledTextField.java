package components;

import utils.UIStyles;
import javax.swing.*;

public class StyledTextField extends JTextField {

    public StyledTextField() {
        setFont(UIStyles.FUENTE_TEXTO);
        setBorder(UIStyles.BORDE_INPUT);
    }
}
