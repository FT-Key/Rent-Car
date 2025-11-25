package components;

import utils.UIStyles;
import javax.swing.*;

public class StyledMenuBar extends JMenuBar {
    public StyledMenuBar() {
        setBackground(UIStyles.COLOR_BOTON);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}
