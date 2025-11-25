package components;

import utils.UIStyles;
import javax.swing.*;
import java.awt.*;

public class StyledPanel extends JPanel {

    public StyledPanel(LayoutManager layout) {
        super(layout);
        setBackground(UIStyles.COLOR_FONDO_PANEL);
        setBorder(UIStyles.BORDE_PANEL);
    }

    public StyledPanel() {
        this(new BorderLayout());
    }
}
