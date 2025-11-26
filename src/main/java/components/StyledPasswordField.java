package components;

import utils.UIStyles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class StyledPasswordField extends JPasswordField {

    public StyledPasswordField() {

        setFont(UIStyles.FUENTE_TEXTO);
        setBorder(UIStyles.BORDE_INPUT);
        setBackground(Color.WHITE);
        setForeground(UIStyles.COLOR_TEXTO_PRINCIPAL);
        setPreferredSize(new Dimension(180, 28));
        setEchoChar('•'); // Punto típico para contraseñas

        // Efecto focus: cambia el borde al seleccionar
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(BorderFactory.createLineBorder(UIStyles.COLOR_SELECCION, 2));
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(UIStyles.BORDE_INPUT);
            }
        });
    }
}
