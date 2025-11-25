package utils;

import javax.swing.*;

public class UIUtils {

    public static void showInfo(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmarSalida() {
        int r = JOptionPane.showConfirmDialog(null, "¿Salir del programa?", "Confirmar", JOptionPane.YES_NO_OPTION);
        return r == JOptionPane.YES_OPTION;
    }
}
