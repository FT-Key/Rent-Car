package components;

import utils.UIStyles;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class StyledTable extends JTable {

    public StyledTable(DefaultTableModel model) {
        super(model);

        // Estilo general
        setFont(UIStyles.FUENTE_TEXTO);
        setRowHeight(25);
        setGridColor(UIStyles.COLOR_BORDE_INPUT);
        setSelectionBackground(UIStyles.COLOR_SELECCION);
        setSelectionForeground(Color.WHITE);
        setShowVerticalLines(false);
        setFillsViewportHeight(true);

        // Intercalar colores de fila
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
            ) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0
                            ? Color.WHITE
                            : UIStyles.COLOR_FILA_ALTERNADA);
                    c.setForeground(UIStyles.COLOR_TEXTO_PRINCIPAL);
                }
                return c;
            }
        });

        // Estilo del header
        JTableHeader header = getTableHeader();
        header.setBackground(UIStyles.COLOR_CABECERA_TABLA);
        header.setForeground(UIStyles.COLOR_CABECERA_TEXTO);
        header.setFont(UIStyles.FUENTE_LABEL);
        header.setReorderingAllowed(false);
    }

    public StyledTable() {
        this(new DefaultTableModel());
    }
}
