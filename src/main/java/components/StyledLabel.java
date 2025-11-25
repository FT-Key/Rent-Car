package components;

import utils.UIStyles;
import javax.swing.*;

public class StyledLabel extends JLabel {

    public StyledLabel(String text) {
        super(text);
        setFont(UIStyles.FUENTE_LABEL);
        setForeground(UIStyles.COLOR_TEXTO_PRINCIPAL);
    }
}
