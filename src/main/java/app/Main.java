package app;

import view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // No aplicamos Look & Feel para que los estilos personalizados de los JFrame se mantengan
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = new MainFrame();
            mf.setVisible(true); // Muestra el JFrame con tus estilos personalizados
        });
    }
}
