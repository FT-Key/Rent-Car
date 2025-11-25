package components;

import utils.UIStyles;
import javax.swing.*;
import java.awt.*;

public class StyledScrollPane extends JScrollPane {

    public StyledScrollPane(Component view) {
        super(view);
        setBorder(BorderFactory.createLineBorder(UIStyles.COLOR_BORDE_INPUT, 1));
        getViewport().setBackground(UIStyles.COLOR_FONDO_PANEL);

        // Altura preferida, mínima y máxima para que ocupe 400 px
        setPreferredSize(new Dimension(700, 400));
        setMinimumSize(new Dimension(700, 400));
        setMaximumSize(new Dimension(700, 400));
    }
}
